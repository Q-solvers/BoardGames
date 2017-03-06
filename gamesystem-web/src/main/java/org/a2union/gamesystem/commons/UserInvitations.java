package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.invitation.GameInvitation;
import org.a2union.gamesystem.model.game.invitation.IGameInvitationService;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.User;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import java.util.Iterator;
import java.util.List;


public class UserInvitations implements GridDataSource {
    private IGameInvitationService gameInvitationService;
    private User user;

    private Iterator result;
    private GameZone zone;

    public int getAvailableRows() {
        return gameInvitationService.countUserInvites(zone);
    }

    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        result = gameInvitationService.getUserInvitationsPage(zone, startIndex, endIndex - startIndex + 1).iterator();
    }

    public Object getRowValue(int index) {
        if (result.hasNext())
            return result.next();
        return null;
    }

    public Class getRowType() {
        return GameInvitation.class;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGameInvitationService(IGameInvitationService gameInvitationService, GameZone currentGameZone) {
        this.gameInvitationService = gameInvitationService;
        this.zone = currentGameZone;
    }
}