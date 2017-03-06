/*
 * $Id: UserServiceImpl.java 200 2010-10-14 14:48:43Z iskakoff $
 */
package org.a2union.gamesystem.model.user;

import org.a2union.gamesystem.commons.INotificationResolver;
import org.a2union.gamesystem.model.activation.Activation;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.user.rate.IRateDAO;
import org.a2union.gamesystem.model.user.rate.Rate;
import org.a2union.gamesystem.model.user.passreminder.PassReminder;
import org.a2union.gamesystem.model.user.passreminder.IPassReminderDAO;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;

import java.util.List;

/**
 * @author Iskakoff
 */
@Transactional(propagation = Propagation.REQUIRED)
public class UserServiceImpl implements IUserService {
    private static final Logger log = Logger.getLogger(UserServiceImpl.class);
    private IUserDAO userDAO;
    private PasswordEncoder passwordEncoder;
    private OnlineUserRegistry onlineUserRegistry;
    private IGameService gameService;
    private IRateDAO rateDAO;
    private IPassReminderDAO passDAO;
    private SimpleMailMessage templateMessage;
    private JavaMailSender mailSender;
    private INotificationResolver notificationResolver;


    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public boolean checkForPassword(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        return passwordEncoder.isPasswordValid(user.getLogin().getPassword(), password, null);
    }

    public User getByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    public User getById(String id) {
        return userDAO.getById(id);
    }

    public String save(User obj) {
        obj.setNotificationMap(15);
        Activation act = new Activation();
        act.setActivationCode(Long.toHexString(RandomUtils.nextLong()));
        obj.setActivation(act);
        act.setPerson(obj);
        GameZone zone = gameService.getMainZone();
        obj.setDefaultZone(zone);
        for (GameZoneType zoneType : GameZoneType.values()) {
            Rate rate = new Rate();
            rate.setUser(obj);
            rate.setZoneType(zoneType);
            rate.setValue(zoneType.getInitialRate());
            obj.addUserRate(rate);
        }
        //We should use some transaction container for transactional scheduling of the jobs.
        //Until this we must use the cron trigger
//        //TODO a. rewrite for adecvate exception handling
//        //TODO b. add transactional support to scheduler
//        try {
//            activationTrigger.setStartDelay(1000);
//            activationTrigger.setName(uuid);
//            activationTrigger.setJobName(uuid);
//            activationTrigger.getJobDetail().setName(uuid);
//            scheduler.addJob(activationTrigger.getJobDetail(), true);
//            scheduler.scheduleJob(activationTrigger);
//        } catch (SchedulerException e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
        return userDAO.save(obj);
    }

    public void update(User obj) {
        userDAO.update(obj);
    }

    public void delete(User obj) {
        userDAO.delete(obj);
    }

    public Object[] getOnlineUsers() {
        return new Object[]{};//sessionRegistry.getAllPrincipals();
    }


    public User getCurrentUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        if(context == null)
            return null;
        Authentication authentication = context.getAuthentication();
        if(authentication == null)
            return null;
        UserDetails principal = (UserDetails) authentication.getPrincipal();
        if(principal == null)
            return null;
        try {
            return getByUsername(principal.getUsername());
        } catch (UsernameNotFoundException e) {
            return null;
        }
    }

    @Override
    public int usersCount() {
        return userDAO.countObjects();
    }

    @Override
    public List<User> getOtherUsers(String username) {
        return userDAO.getOtherUsers(username);
    }

    @Override
    public List<User> getOtherUsersOrdered(String username, String order) {
        return userDAO.getOtherUsersOrdered(username, order);
    }

    @Override
    public Integer getUserRateValue(User player, GameZone gameZone) {
        Rate userRate = getUserRate(player, gameZone);
        return userRate != null ? userRate.getValue() : 0;
    }

    @Override
    public Rate getUserRate(User player, GameZone gameZone) {
        return rateDAO.getRateByUserAndZone(player, gameZone);
    }

    @Override
    public boolean isUserOnline(User user) {
        return user != null && onlineUserRegistry.isUserOnline(user.getLogin().getUsername());
    }

    @Override
    public void updateUserDetails(User person, String password, boolean movement, boolean invitation) {
        if (StringUtils.isNotBlank(password)) {
            updateUserPassword(person, password);
        }
        int map = (movement?1:0) + (invitation? 14:0);
        person.setNotificationMap(map);
    }

    @Override
    public void restorePassword(String passId, String password) {
        PassReminder passReminder = passDAO.getById(passId);
        updateUserPassword(passReminder.getUser(), password);
        passDAO.delete(passReminder);
    }

    @Override
    public boolean remindPassword(String email) {
        User user = userDAO.getUserByEmail(email);
        if(user == null)
            return false;
        try {
            PassReminder passReminder = new PassReminder();
            passReminder.setUser(user);
            passDAO.save(passReminder);
            notificationResolver.notifyUser(user, notificationResolver.getPasswordReminderNotifiaction(user.getUUID(), passReminder.getUUID()));
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public PassReminder geyPasswordReminder(String passId) {
        return passDAO.getById(passId);
    }

    @Override
    public boolean isUserExist(String email) {
        return userDAO.getUserByEmail(email)!=null;
    }

    @Override
    public List<User> getOtherUsersPagedOrdered(String username, String order, int startIndex, int length) {
        return userDAO.getOtherUsersPagedOrdered(username, order, startIndex, length);
    }

    private void updateUserPassword(User user, String password) {
        Login login = user.getLogin();
        login.setPassword(passwordEncoder.encodePassword(password, null));
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public void setGameService(IGameService gameService) {
        this.gameService = gameService;
    }

    public void setRateDAO(IRateDAO rateDAO) {
        this.rateDAO = rateDAO;
    }


    public void setOnlineUserRegistry(OnlineUserRegistry onlineUserRegistry) {
        this.onlineUserRegistry = onlineUserRegistry;
    }


    public void setPassDAO(IPassReminderDAO passDAO) {
        this.passDAO = passDAO;
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }


    public void setNotificationResolver(INotificationResolver notificationResolver) {
        this.notificationResolver = notificationResolver;
    }

    
}
