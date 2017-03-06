package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.AttackPawnStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.IPStartegy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.PawnExchangeStrategy;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.PawnStrategy;

/**
 * @author Iskakoff
 */
public class Pawn extends BaseChessType {

    public MoveStrategy getStrategy() {
        if (isInAttack() && !isPawnExchange()) return new AttackPawnStrategy();
        else if (isPawnExchange()) return new PawnExchangeStrategy();
        else return isInitialStep() ?
                    new IPStartegy() : new PawnStrategy();
    }

    public String getType() {
        return "p";
    }

}
