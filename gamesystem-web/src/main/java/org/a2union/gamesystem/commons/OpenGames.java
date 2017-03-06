package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.user.User;
import org.apache.tapestry5.grid.GridDataSource;
import org.apache.tapestry5.grid.SortConstraint;

import java.util.Iterator;
import java.util.List;


public class OpenGames implements GridDataSource {
    private IGameService gameService;
    private String gameZoneId;
    private User user;

    private Iterator result;

    public int getAvailableRows() {
        return gameService.countOpenGames(gameZoneId, user);
    }

    public Object getRowValue(int index) {
        if (result.hasNext())
            return result.next();
        return null;
    }

    public Class getRowType() {
        return GameBase.class;
    }

    public void prepare(int startIndex, int endIndex, List<SortConstraint> sortConstraints) {
        result = gameService.getOpenGamesPage(gameZoneId, user, startIndex, endIndex - startIndex + 1).iterator();
    }

    public void setGameZone(String gameZoneId) {
        this.gameZoneId = gameZoneId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setGameService(IGameService gameService) {
        this.gameService = gameService;
    }
}
