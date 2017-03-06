package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;

import java.util.Map;

/**
 * Linear move strategy
 *
 * @author Iskakoff
 */
public class LinearStrategy extends StrategyBase {

    @Override
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return (currentLetter.equals(newLetter) && !currentLine.equals(newLine))
                || (!currentLetter.equals(newLetter) && currentLine.equals(newLine))  ? this : null;
    }

    @Override
    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        return checkHorizontalMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map) ||
                checkVerticalMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map);
    }
}
