package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.game.zone.GameZoneType;

/**
 * @author Iskakoff
 */
public interface IBoardGame {

    void setGameId(String gameId);
    GameZoneType getGameZoneType();
}
