package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;

import java.util.Map;

/**
 * @author Iskakoff
 */
public class InitialPawnStrategy extends StrategyBase {
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return currentLetter.equals(newLetter) && (newLine == 4 || newLine == 5) && Math.abs(newLine - currentLine) == 2 ? this : null;
    }

    @Override
    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        return map.get(convertLetterNumber(newLetter) + newLine) == null &&
                map.get(convertLetterNumber(newLetter) + (newLine > currentLine ? (newLine - 1) : newLine + 1)) == null;
    }
}
