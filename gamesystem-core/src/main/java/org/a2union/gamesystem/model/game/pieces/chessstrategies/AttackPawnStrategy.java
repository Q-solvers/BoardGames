package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;

import java.util.Map;

/**
 * @author Iskakoff
 */
public class AttackPawnStrategy extends StrategyBase {
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return Math.abs(newLetter - currentLetter) == 1 && ((newLine - currentLine) == (isFirst ?  1 : -1)) ? this : null;
    }

    @Override
    protected boolean checkForFree(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isInAttack) {
//        Map<String, IChessPiece> nextMap = gameSide.getNext().getPiecesMap();
        return piecesMap.get(convertLetterNumber(newLetter) + newLine) == null/* && nextMap.get(convertLetterNumber(newLetter) + newLine) != null*/;
    }

    @Override
    protected void killEnemy(String newPosition, ChessSide gameSide, ChessSide nextSide, Map<String, IChessPiece> map) {
        String position = newPosition;
        int line = Integer.parseInt(newPosition.substring(1, 2));
        Map<String, IChessPiece> piecesMap = nextSide.getPiecesMap();
        IChessPiece iPiece = piecesMap.get(position);
        if(iPiece == null) {
            position = newPosition.substring(0, 1) + (gameSide.isFirst() ? line - 1 : line + 1);
            iPiece = piecesMap.get(position);
        }
        piecesMap.remove(position);
        nextSide.getPiecesIdMap().remove(iPiece.getNumber());
        iPiece.setPosition("kill");
        map.put("kill", iPiece);
    }
}
