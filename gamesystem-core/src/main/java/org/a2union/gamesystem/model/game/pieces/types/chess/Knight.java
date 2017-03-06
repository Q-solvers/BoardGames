package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.KnightStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;

/**
 * @author Iskakoff
 */
public class Knight extends BaseChessType {

    public MoveStrategy getStrategy() {
        return new KnightStrategy();
    }

    public String getType() {
        return "K";
    }
}
