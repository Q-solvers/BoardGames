package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.user.User;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import java.util.Iterator;
import java.util.List;


public class ActiveGames implements GridDataSource {
    private IGameService gameService;
    private String gameZoneId;
    private User user;

    private Iterator result;

    public int getAvailableRows() {
        return gameService.countActiveGames(gameZoneId, user);
    }

    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        result = gameService.getActiveGamesPage(gameZoneId, user, startIndex, endIndex - startIndex + 1).iterator();
    }

    public Object getRowValue(int index) {
        if (result.hasNext())
            return result.next();
        return null;
    }

    public Class getRowType() {
        return GameBase.class;
    }


    public String getGameZone() {
        return gameZoneId;
    }

    public void setGameZone(String gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGameService(IGameService gameService) {
        this.gameService = gameService;
    }
}
