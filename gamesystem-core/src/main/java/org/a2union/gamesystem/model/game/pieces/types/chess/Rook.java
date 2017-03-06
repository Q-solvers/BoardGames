package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.RookStrategy;

/**
 * @author Iskakoff
 */
public class Rook extends BaseChessType {

    public MoveStrategy getStrategy() {
        return new RookStrategy();
    }

    public String getType() {
        return "R";
    }

}
