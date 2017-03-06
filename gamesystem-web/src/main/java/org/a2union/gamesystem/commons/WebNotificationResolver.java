/*
 * $Id: WebNotificationResolver.java 201 2010-10-15 15:11:11Z iskakoff $
 */
package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.side.GameSideType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.util.Assert;

import java.util.ResourceBundle;

/**
 * @author Iskakoff
 */
public class WebNotificationResolver extends NotificationResolver
        implements INotificationResolver, InitializingBean {

    //base server url
    private String url;

    private ResourceBundleMessageSource messageSource;

    public String getActivationNotification(String userId, String activationCode) {
        return messageSource.getMessage("activation-notification", new Object[]{url, userId, activationCode}, null, null);
    }

    @Override
    public String getMovementNotification(String result, String gameId, Object gametype) {
        return messageSource.getMessage("movement-notification", new Object[]{result, url, gametype, gameId}, null, null);
    }

    @Override
    public String getInvitationNotification(String username, String type) {
        return messageSource.getMessage("game-invitation-notification", new Object[]{username, type}, null, null);
    }

    @Override
    public String getInvitationAcceptedNotification(String username, String gameId, String gametype) {
        return messageSource.getMessage("game-invitation-accepted-notification", new Object[]{username, url, gametype, gameId}, null, null);
    }

    @Override
    public String getInvitationDeclineNotification(String username, GameSideType invitedSideType) {
        return messageSource.getMessage("game-invitation-declined-notification", new Object[]{username,
                ResourceBundle.getBundle("localization").getString(invitedSideType.toString())}, null, null);
    }

    public String getPasswordReminderNotifiaction(String uuid, String passUUID) {
        return messageSource.getMessage("password-remind-notification", new Object[]{url, passUUID}, null, null);
    }

    public void setMessageSource(ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * checking for web-url
     *
     * @throws Exception if web-url is null
     */
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(url);
    }
}
