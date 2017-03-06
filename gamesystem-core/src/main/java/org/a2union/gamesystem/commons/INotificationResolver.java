/*
 * $Id: INotifiactionResolver.java 198 2010-09-28 05:36:09Z iskakoff $
 */
package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.user.User;

/**
 * Class resolve notification messages. It`s subclass Should be used as spring-bean <code>notificationResolver</code> to override
 * default notification messages
 *
 * @author Iskakoff
 */
public interface INotificationResolver {

    /**
     * Get activation notification message for user with specified identifier and activation code
     *
     * @param userId         identifier
     * @param activationCode activation code
     * @return notifiaction message text
     */
    String getActivationNotification(String userId, String activationCode);

    String getMovementNotification(String result, String gameId, Object gametype);

    String getInvitationNotification(String username, String type);

    String getInvitationAcceptedNotification(String username, String gameId, String gametype);

    String getInvitationDeclineNotification(String username, GameSideType invitedSideType);

    //notify user2 that user1 has invited him
    void sendInvitationNotification(User user1, User user2, GameSideType invitedSideType);

    //notify user2 that user1 has accepted the invitation
    void sendInvitationAcceptedNotification(User user1, User user2, String gameId, String gametype);

    //notify user2 that user1 has declined the invitation
    void sendInvitationDeclineNotification(User user1, User user2, GameSideType invitedSideType);

    //send e-mail notification to user
    void notifyUser(User user, String message);

    void sendMovementNotification(User user, String result, String gameId, Object gametype);

    void remindPassword(String email);

    String getPasswordReminderNotifiaction(String uuid, String passUUID);
}
