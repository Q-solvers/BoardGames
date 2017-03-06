package org.a2union.gamesystem.web.pages.reversi;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.game.reversi.IReversiGameService;
import org.a2union.gamesystem.commons.IBoardGame;
import org.a2union.gamesystem.web.pages.Index;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Brutal copy-paste :-)
 *
 * @author Iskakoff
 */
public class ReversiGame implements IBoardGame {
    private GameBase game;
    @Inject
    private IReversiGameService reversiGameService;
    private String gameId;

    public void onActivate(String gameUUID) {
        gameId = gameUUID;
        game = reversiGameService.getGame(gameUUID);
        if(game==null || !getGameZoneType().equals(game.getZone().getType()))
            throw new IllegalStateException("game is incorrect");
    }

    public void onActivate() {
        if(gameId != null)
            onActivate(gameId);
        else
            throw new IllegalStateException("game id must be specified");
    }

    public String onPassivate() {
        return game!= null ? game.getUUID(): gameId;
    }

    public GameBase getGame() {
        return game;
    }

    public void setGame(GameBase game) {
        this.game = game;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    @Override
    public GameZoneType getGameZoneType() {
        return GameZoneType.REVERSI_ZONE;
    }
}
