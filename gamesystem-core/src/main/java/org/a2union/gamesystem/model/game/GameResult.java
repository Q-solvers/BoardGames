package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.model.game.side.GameSideType;

/**
 * @author Iskakoff
 */
public enum GameResult {
    WHITE_WON("1-0", GameSideType.FIRST), BLACK_WON("0-1", GameSideType.SECOND), DRAW("1/2-1/2");
    private String result;
    private GameSideType sideType;

    GameResult(String result, GameSideType sideType) {
        this.result = result;
        this.sideType = sideType;
    }

    GameResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public GameSideType getSideType() {
        return sideType;
    }
}
