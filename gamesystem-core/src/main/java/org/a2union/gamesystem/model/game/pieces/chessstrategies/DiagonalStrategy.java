package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;

import java.util.Map;

/**
 * Diagonal strategy
 *
 * @author Iskakoff
 */
public class DiagonalStrategy extends StrategyBase {

    @Override
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return (currentLetter - newLetter != 0) && (Math.abs(currentLetter - newLetter) == Math.abs(currentLine - newLine)) ? this : null;
    }

    @Override
    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        return checkDiagonalMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map);
    }
}
