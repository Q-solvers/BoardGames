package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;

import java.util.Map;

/**
 * @author Iskakoff
 */
public class ComplexStrategy extends StrategyBase {
    private MoveStrategy[] strategies;

    public ComplexStrategy(MoveStrategy ... strategies) {
        this.strategies = strategies;
    }

    @Override
    public MoveStrategy isInStrategy(String currentPosition, String newPosition,
                                     Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isInAttack, boolean isFirst) {
        for (MoveStrategy strategy : strategies) {
            if(strategy.isInStrategy(currentPosition, newPosition, piecesMap, enemyPiecesMap, isInAttack, isFirst) != null )
                return strategy;
        }
        return null;
    }
}
