package org.a2union.gamesystem.model.game.pieces;

import java.util.Map;

/**
 * @author Iskakoff
 */
public class ReversiStrategy implements IReversiStrategy {
    private Map<String, IReversiPiece> reversiPieceMap;

    @Override
    public void setMap(Map<String, IReversiPiece> reversiPieceMap) {
        this.reversiPieceMap = reversiPieceMap;
    }

    public Map<String, IReversiPiece> getReversiPieceMap() {
        return reversiPieceMap;
    }
}
