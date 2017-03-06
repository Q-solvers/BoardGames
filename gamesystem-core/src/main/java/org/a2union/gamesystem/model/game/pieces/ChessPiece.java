package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;

import java.util.Map;

/**
 * @author Iskakoff
 */
public class ChessPiece implements IChessPiece {

    private String position;
    private String oldPosition;
    private String number;
    private IChessPieceType pieceType;


    public MoveStrategy checkMove(String newPosition,
                                  Map<String, ? extends IPiece> piecesMap, Map<String, ? extends IPiece> enemyPiecesMap,
                                  boolean isFirst) {
        // we can attack only that places where enemy pieces stand
        Map<String, IChessPiece> chessPieceMap = (Map<String, IChessPiece>) piecesMap;
        Map<String, IChessPiece> chessEnemyPieceMap = (Map<String, IChessPiece>) enemyPiecesMap;
        if (isInAttack()) {
            if (!enemyPiecesMap.containsKey(newPosition))
                return null;
        }
        if ("p".equals(getPieceType().getType())) {
            Integer letter = CommonChessUtils.getInstance().convertLetter(newPosition.substring(0, 1));
            Integer line = Integer.parseInt(newPosition.substring(1, 2));
            IChessPiece piece = (IChessPiece) enemyPiecesMap.get(CommonChessUtils.getInstance().convertLetterNumber(letter) + (isFirst ? line - 1 : line + 1));
            if (piece != null && piece.getPieceType().getType().equals("p") && !piece.getPieceType().isInitialStep()) {
                int oldP = Integer.parseInt(piece.getOldPosition().substring(1, 2));
                int newP = Integer.parseInt(piece.getPosition().substring(1, 2));
                setInAttack(isInAttack() || Math.abs(newP - oldP) == 2);
            }
            if ((isFirst && line == 8) || (!isFirst && line == 1)) {
                setPawnExchange(true);
            }
        }
        MoveStrategy moveStrategy = getPieceType().getStrategy().isInStrategy(getPosition(), newPosition, chessPieceMap, chessEnemyPieceMap, isInAttack(), isFirst);
        if (moveStrategy != null)
            return moveStrategy;
        return null;
    }

    public boolean isInitialStep() {
        return getPieceType().isInitialStep();
    }

    public void setInitialStep(boolean initialStep) {
        getPieceType().setInitialStep(initialStep);
    }

    public void setInitial(String initial) {
        setInitialStep(Boolean.parseBoolean(initial));
    }

    public void move(String newPosition) {
        setInitialStep(false);
        setOldPosition(getPosition());
        setPosition(newPosition);
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public String getOldPosition() {
        //quick fix
        if (oldPosition == null) {
            return position;
        }
        return oldPosition;
    }

    public void setOldPosition(String oldPosition) {
        this.oldPosition = oldPosition;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean isInAttack() {
        return getPieceType().isInAttack();
    }

    public void setInAttack(boolean inAttack) {
        getPieceType().setInAttack(inAttack);
    }

    private void setPawnExchange(boolean pawnExchange) {
        getPieceType().setPawnExchange(pawnExchange);
    }

    /**
     * Piece string representation format:
     * type:<type>;number:<number>;position:<position>[;initial:<is initial position>]
     * <p/>
     * types:
     * R - rook
     * K - knight
     * B - bishop
     * Q - queen
     * Kp - king
     * p - pawn
     *
     * @return piece string representation
     */
    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("type:");
        buffer.append(getPieceType().getType());
        buffer.append(";");
        buffer.append("number:");
        buffer.append(getNumber());
        buffer.append(";");
        buffer.append("position:");
        buffer.append(getPosition());
        buffer.append(";");
        buffer.append("oldPosition:");
        buffer.append(getOldPosition());
        buffer.append(";initial:");
        buffer.append(isInitialStep());
        return buffer.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof ChessPiece) {
            ChessPiece piece = (ChessPiece) obj;
            return piece.getPosition().equals(getPosition()) && piece.getNumber().equals(getNumber());
        }
        return false;
    }

    public IChessPieceType getPieceType() {
        return pieceType;
    }

    public void setPieceType(IChessPieceType pieceType) {
        this.pieceType = pieceType;
    }
}
