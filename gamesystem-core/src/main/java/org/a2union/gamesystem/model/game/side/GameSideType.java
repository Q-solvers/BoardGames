/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game.side;

/**
 * For chess game first is white, second is black
 * @author Iskakoff
 */
public enum GameSideType {
    FIRST("first"), SECOND("second");

    private final String type;

    GameSideType(String type) {
        this.type=type;
    }

    public String getType() {
        return type;
    }
}
