package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.CastleStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.ComplexStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.KingStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;

/**
 * @author Iskakoff
 */
public class King extends BaseChessType {
    public MoveStrategy getStrategy() {
        return isInitialStep() ? new ComplexStrategy(new KingStrategy(), new CastleStrategy()) : new KingStrategy();
    }

    public String getType() {
        return "Kp";
    }
}
