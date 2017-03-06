package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.BishopStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;

/**
 * @author Iskakoff
 */
public class Bishop extends BaseChessType {
    public MoveStrategy getStrategy() {
        return new BishopStrategy();
    }

    public String getType() {
        return "B";
    }
}
