package org.a2union.gamesystem.model.game.pieces.types.chess;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;

/**
 * @author Iskakoff
 */
public interface IChessPieceType extends IPieceType {
    MoveStrategy getStrategy();
//
//    String getPosition();
//    void setPosition(String s);

    boolean isInAttack();

    void setInAttack(boolean inAttack);

    boolean isPawnExchange();

    void setPawnExchange(boolean pawnExchange);

    boolean isInitialStep();

    void setInitialStep(boolean initialStep);
//    String getNumber();

}
