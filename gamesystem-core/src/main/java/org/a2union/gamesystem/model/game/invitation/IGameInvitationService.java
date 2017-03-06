package org.a2union.gamesystem.model.game.invitation;

import org.a2union.gamesystem.model.base.IBaseService;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.User;

import java.util.List;

/**
 * Service to work with user invitations
 *
 * @author Iskakoff
 */
public interface IGameInvitationService extends IBaseService<GameInvitation> {
    /**
     * Current logged in user invite another user in challenge in specified game zone
     * @param user - user to invite
     * @param zone - zone to invite user in challenge
     * @param invitingSideType - side of inviting user
     * @param invitedSideType - side of invited user
     * @return invitation Id
     */
    String inviteUser(User user, GameZone zone, GameSideType invitingSideType, GameSideType invitedSideType);

    /**
     * Accept specified invitation
     * Create new accepted game from current invitation
     * @param invitation - invitation to accept
     * @return accepted game id
     */
    String acceptInvitation(GameInvitation invitation);

    /**
     * Decline specified invitation
     * @param invitation - declining invitation
     */
    void declineInvitation(GameInvitation invitation);

    /**
     * @return Count unread invites of current user
     * @param zone
     */
    int countUserInvites(GameZone zone);

    List<GameInvitation> getUserInvitationsPage(GameZone zone, int startIndex, int size);
}
