package org.a2union.gamesystem.model.game.invitation;

import org.a2union.gamesystem.commons.INotificationResolver;
import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Iskakoff
 */
public class GameInvitationService implements IGameInvitationService {
    private IGameInvitationDAO gameInvitationDAO;
    private IUserService userService;
    private IGameService<GameBase, IPieceType, IPiece<IPieceType>> gameService;
    private INotificationResolver notificationResolver;

    @Override
    @Transactional
    public String inviteUser(User user, GameZone zone, GameSideType invitingSideType, GameSideType invitedSideType) {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null) {
            throw new IllegalStateException("There is no user in online. Nobody can't invite somebody");
        }
        if(user == null) {
            throw new IllegalStateException("You can't invite nobody");
        }
        if(currentUser.equals(user)) {
            throw new IllegalStateException("You can't invite yourself");
        }
        if(invitedSideType == null || invitingSideType == null) {
            throw new IllegalStateException("Both side must be specified;");
        }
        if(invitedSideType.equals(invitingSideType))
            throw new IllegalStateException("You can't play at the same side as your opponent");
        String invitation = gameInvitationDAO.createInvitation(user, currentUser, zone, invitingSideType, invitedSideType);
        notificationResolver.sendInvitationNotification(currentUser, user, invitedSideType);
        return invitation;
    }

    @Override
    @Transactional
    public String acceptInvitation(GameInvitation invitation) {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null) {
            throw new IllegalStateException("Nobody has no invitations");
        }
        GameBase base = new GameBase();
        User user = invitation.getInvitingUser();
        base.setGameName(user.getLogin().getUsername() + " VS " + invitation.getInvitedUser().getLogin().getUsername());
        base.setZone(invitation.getInvitedGameZone());
        gameService.createGame(base, invitation.getInvitedingUserSideType(), invitation.getInvitingUser());
        String id = base.getUUID();
        gameService.acceptGame(id, invitation.getInvitedUser());
        gameInvitationDAO.delete(invitation);
        String type = invitation.getInvitedGameZone().getType().getValue();
        notificationResolver.sendInvitationAcceptedNotification(currentUser, user, id,  type + "/" + type +"game");
        return id;
    }

    @Override
    @Transactional
    public void declineInvitation(GameInvitation invitation) {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null) {
            throw new IllegalStateException("Nobody has no invitations");
        }
        User user = invitation.getInvitingUser();
        GameSideType sideType = invitation.getInvitedUserSideType();
        gameInvitationDAO.delete(invitation);
        notificationResolver.sendInvitationDeclineNotification(currentUser, user, sideType);
    }

    @Override
    public int countUserInvites(GameZone zone) {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null) {
            throw new IllegalStateException("Nobody has no invitations");
        }
        return gameInvitationDAO.countInvitationsByUser(currentUser, zone, true);
    }

    @Override
    public List<GameInvitation> getUserInvitationsPage(GameZone zone, int startIndex, int size) {
        User currentUser = userService.getCurrentUser();
        if(currentUser == null) {
            throw new IllegalStateException("Nobody has no invitations");
        }
        return gameInvitationDAO.getInvitationsByUserPage(currentUser, zone, startIndex, size);
    }

    @Override
    public GameInvitation getById(String id) {
        return gameInvitationDAO.getById(id);
    }

    @Override
    @Transactional
    public String save(GameInvitation obj) {
        return gameInvitationDAO.save(obj);
    }

    @Override
    @Transactional
    public void update(GameInvitation obj) {
        gameInvitationDAO.update(obj);
    }

    @Override
    @Transactional
    public void delete(GameInvitation obj) {
        gameInvitationDAO.delete(obj);
    }


    public void setGameInvitationDAO(IGameInvitationDAO gameInvitationDAO) {
        this.gameInvitationDAO = gameInvitationDAO;
    }


    public void setUserService(IUserService userService) {
        this.userService = userService;
    }


    public void setGameService(IGameService gameService) {
        this.gameService = gameService;
    }


    public void setNotificationResolver(INotificationResolver notificationResolver) {
        this.notificationResolver = notificationResolver;
    }

}
