/*
 * $Id: ActivationService.java 201 2010-10-15 15:11:11Z iskakoff $
 */
package org.a2union.gamesystem.model.activation;

import org.a2union.gamesystem.commons.INotificationResolver;
import org.a2union.gamesystem.model.user.exceptions.ActivationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Service for work with user acticvation mechanisms
 *
 * @author Iskakoff
 */
public class ActivationService implements IActivationService {

    private static final int PAGE_SIZE = 5;

    private IActivationDAO activationDAO;
    private SimpleMailMessage mailTemplate;
    private JavaMailSender mailer;
    private INotificationResolver notificationResolver;


    public Activation getById(String id) {
        return activationDAO.getById(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String save(Activation obj) {
        return activationDAO.save(obj);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(Activation obj) {
        activationDAO.update(obj);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Activation obj) {
        activationDAO.delete(obj);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void activateUser(String userId, String activationCode) throws ActivationException {
        Activation code = activationDAO.getActivationByUserAndCode(userId, activationCode);
        if (code == null)
            throw new ActivationException();
        code.getPerson().getLogin().setActive(true);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void sendActivation() {
        List<Activation> activations = activationDAO.getNonActiveActivations(PAGE_SIZE);
        List<SimpleMailMessage> msgs = new ArrayList<SimpleMailMessage>(PAGE_SIZE);
        for (Activation activation : activations) {
            SimpleMailMessage mail = new SimpleMailMessage(mailTemplate);
            mail.setTo(activation.getPerson().getEmail());
            mail.setText(notificationResolver.getActivationNotification(
                    activation.getPerson().getUUID(), activation.getActivationCode()));
            msgs.add(mail);
            activation.setNotified(true);
        }
        if (msgs.isEmpty())
            return;
        mailer.send(msgs.toArray(new SimpleMailMessage[msgs.size()]));
    }

    public void setMailSender(JavaMailSenderImpl mailSender) {
        this.mailer = mailSender;
    }

    public void setTemplateMessage(SimpleMailMessage templateMessage) {
        this.mailTemplate = templateMessage;
    }

    public void setActivationDAO(IActivationDAO activationDAO) {
        this.activationDAO = activationDAO;
    }

    public void setNotificationResolver(INotificationResolver notificationResolver) {
        this.notificationResolver = notificationResolver;
    }
}
