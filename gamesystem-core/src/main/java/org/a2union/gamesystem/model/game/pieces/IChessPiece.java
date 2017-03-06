package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;

import java.util.Map;

/**
 * @author Iskakoff
 */
public interface IChessPiece extends IPiece<IChessPieceType> {

    String getOldPosition();
    void setOldPosition(String s);

    boolean isInAttack();
    void setInAttack(boolean inAttack);

}
