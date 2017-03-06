/*
 * : $
 */
package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.user.User;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.quartz.JobDetailBean;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Iskakoff
 */
public class NotificationResolver implements INotificationResolver {
    private ResourceBundleMessageSource messageSource;
    private SimpleMailMessage templateMessage;
    private Scheduler scheduler;
    private JobDetailBean movementNotifierJob;

    public String getActivationNotification(String userId, String activationCode) {
        return messageSource.getMessage("activation-notification", null, null, null);
    }

    @Override
    public String getMovementNotification(String result, String gameId, Object gametype) {
        return messageSource.getMessage("movement-notification", new Object[]{result}, null, null);
    }

    @Override
    public String getInvitationNotification(String username, String type) {
        return messageSource.getMessage("game-invitation-notification", new Object[]{username, type}, null, null);
    }

    @Override
    public String getInvitationAcceptedNotification(String username, String gameId, String gametype) {
        return messageSource.getMessage("game-invitation-accepted-notification", new Object[]{username}, null, null);
    }

    @Override
    public String getInvitationDeclineNotification(String username, GameSideType invitedSideType) {
        return messageSource.getMessage("game-invitation-declined-notification", new Object[]{username,
                ResourceBundle.getBundle("localization").getString(invitedSideType.toString())}, null, null);
    }

    @Override
    public void sendInvitationNotification(User user1, User user2, GameSideType invitedSideType) {
        String s = getInvitationNotification(user1.getLogin().getUsername(),
                ResourceBundle.getBundle("localization").getString(invitedSideType.toString()));
        if((user2.getNotificationMap()&2) != 0)
            notifyUser(user2, s);
    }

    @Override
    public void sendInvitationAcceptedNotification(User user1, User user2, String gameId, String gametype) {
        String s = getInvitationAcceptedNotification(user1.getLogin().getUsername(), gameId, gametype);
        if((user2.getNotificationMap()&4) != 0)
            notifyUser(user2, s);
    }

    @Override
    public void sendInvitationDeclineNotification(User user1, User user2, GameSideType invitedSideType) {
        String s = getInvitationDeclineNotification(user1.getLogin().getUsername(),invitedSideType);
        if((user2.getNotificationMap()&8) != 0)
            notifyUser(user2, s);
    }

    @Override
    public void sendMovementNotification(User user, String result, String gameId, Object gametype) {
        String s = getMovementNotification(result, gameId, gametype);
        if((user.getNotificationMap()&1) != 0)
            notifyUser(user, s);
    }

    @Override
    public void remindPassword(String email) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getPasswordReminderNotifiaction(String uuid, String passUUID) {
        return messageSource.getMessage("password-remind-notification", new Object[]{passUUID}, null, null);
    }

    public void notifyUser(User user, String message) {
        final SimpleMailMessage mail = new SimpleMailMessage(templateMessage);
        mail.setTo(user.getEmail());
        mail.setText(message);
        JobDetail job = (JobDetail) movementNotifierJob.clone();
        job.getJobDataMap().put("message", mail);
        job.setName(job.getName()+ System.currentTimeMillis());
        try {
            SimpleTrigger simpleTrigger = new SimpleTrigger();
            simpleTrigger.setName("simple"+ System.currentTimeMillis());
            simpleTrigger.setStartTime(new Date());
            simpleTrigger.setRepeatCount(0);
            scheduler.scheduleJob(job, simpleTrigger);
        } catch (SchedulerException e) {
            //TODO some error handilng
            e.printStackTrace();
        }
    }

    public void setMessageSource(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.templateMessage = templateMessage;
    }


    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }


    public void setMovementNotifierJob(JobDetailBean movementNotifierJob) {
        this.movementNotifierJob = movementNotifierJob;
    }
}
