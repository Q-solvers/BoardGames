package org.a2union.gamesystem.model.game.invitation;

import org.a2union.gamesystem.model.base.IBaseDAO;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.User;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface IGameInvitationDAO extends IBaseDAO<GameInvitation> {
    String createInvitation(User invitedUser, User invitingUser, GameZone zone, GameSideType invitingSideType, GameSideType invitedSideType);

    /**
     * Count invitations invites current user
     *
     * @param currentUser - invited user
     * @param zone
     *@param unread - obtain only newest invitation if true @return count of current user invitations
     */
    int countInvitationsByUser(User currentUser, GameZone zone, boolean unread);

    List<GameInvitation> getInvitationsByUserPage(User currentUser, GameZone zone, int startIndex, int size);
}
