package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Iskakoff
 */
public class CastleStrategy extends StrategyBase {

    @Override
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        if (((newLine - currentLine) != 0) || (Math.abs(newLetter - currentLetter) != 2))
            return null;
        boolean inCheck = false;
        if (newLetter == 7) {
            inCheck |= !checkForCheck(convertLetterNumber(5) + newLine, enemyPiecesMap, piecesMap, isFirst);
            inCheck |= !checkForCheck(convertLetterNumber(6) + newLine, enemyPiecesMap, piecesMap, isFirst);
            inCheck |= !checkForCheck(convertLetterNumber(7) + newLine, enemyPiecesMap, piecesMap, isFirst);
        } else if (newLetter == 3) {
            inCheck |= !checkForCheck(convertLetterNumber(5) + newLine, enemyPiecesMap, piecesMap, isFirst);
            inCheck |= !checkForCheck(convertLetterNumber(4) + newLine, enemyPiecesMap, piecesMap, isFirst);
            inCheck |= !checkForCheck(convertLetterNumber(3) + newLine, enemyPiecesMap, piecesMap, isFirst);
        }

        if (!inCheck && ((newLetter == 7) || (newLetter == 3))) {
            IChessPiece iPiece = piecesMap.get((newLetter == 7 ? "H" : "A") + newLine);
            if ("R".equals(iPiece.getPieceType().getType()) && iPiece.getPieceType().isInitialStep()) {
                return this;
            }
        }
        return null;
    }

    @Override
    public String movement(String oldPosition, String newPosition, boolean isAttack, String... newType) {
        return "G".equalsIgnoreCase(newPosition.substring(0,1)) ? "O-O" : "O-O-O";
    }

    @Override
    public Map<String, IChessPiece> movePiece(String newPosition, IChessPiece iPiece, ChessSide gameSide, ChessSide nextSide) {
        Integer newLetter = convertLetter(StringUtils.substring(newPosition, 0, 1));
        Integer newLine = Integer.parseInt(StringUtils.substring(newPosition, 1, 2));
        IChessPiece piece = gameSide.getPiecesMap().remove((newLetter == 7 ? "H" : "A") + newLine);
        piece.move((newLetter == 7 ? "F" : "D") + newLine);
        gameSide.getPiecesMap().remove(iPiece.getPosition());
        iPiece.move(newPosition);
        gameSide.getPiecesMap().put(newPosition, iPiece);
        gameSide.getPiecesMap().put(piece.getPosition(), piece);
        HashMap<String, IChessPiece> map = new HashMap<String, IChessPiece>();
        map.put(iPiece.getNumber(), iPiece);
        map.put(piece.getNumber(), piece);
        return map;
    }

    @Override
    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        if (newLetter == 7) {
            return map.get(convertLetterNumber(6) + newLine) == null &&
                    map.get(convertLetterNumber(7) + newLine) == null;
        }
        return newLetter == 3 && map.get(convertLetterNumber(4) + newLine) == null &&
                map.get(convertLetterNumber(3) + newLine) == null &&
                map.get(convertLetterNumber(2) + newLine) == null;
    }
}
