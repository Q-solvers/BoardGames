package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.types.IPieceType;

import java.util.Map;

/**
 * @author Iskakoff
 */
public interface IPiece<I extends IPieceType> {
    /**
     * Method return the strategy with which piece can be moved to new position
     * null otherwise
     *
     * @param newPosition    - new position of Piece
     * @param piecesMap      - current game side pieces
     * @param enemyPiecesMap - enemy side piceses
     * @param isFirst        - is current side is first
     * @return move strategy
     */
    CommonStrategy checkMove(String newPosition,
                           Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap,
                           boolean isFirst);

    String getPosition();

    void setPosition(String s);

    void move(String newPosition);

    String getNumber();

    void setPieceType(I type);

    I getPieceType();
}
