package org.a2union.gamesystem.model.game.pieces;

import java.util.Map;

/**
 * @author Iskakoff
 */
public interface IReversiStrategy extends CommonStrategy {
    void setMap(Map<String, IReversiPiece> reversiPieceMap);
}
