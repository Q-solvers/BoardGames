package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.ComplexStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.QueenDiagonalStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.QueenLinearStrategy;

/**
 * @author Iskakoff
 */
public class Queen extends BaseChessType {
    public MoveStrategy getStrategy() {
        return new ComplexStrategy(new QueenDiagonalStrategy(), new QueenLinearStrategy());
    }

    public String getType() {
        return "Q";
    }
}
