package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;

import java.util.Map;

/**
 * @author Iskakoff
 */
public class KnightStrategy extends StrategyBase {

    @Override
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine,
                                         Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return (Math.abs(currentLetter - newLetter) == 2 && Math.abs(currentLine - newLine) == 1)
                || (Math.abs(currentLetter - newLetter) == 1 && Math.abs(currentLine - newLine) == 2) ? this : null;
    }

    @Override
    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        return checkKnightMove(currentLetter, currentLine, newLetter, newLine, isInAttack, map);
    }

    @Override
    protected String getLetter() {
        return "N";
    }
}
