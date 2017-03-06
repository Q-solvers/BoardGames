package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;

import java.util.HashMap;
import java.util.Map;


public class PawnExchangeStrategy extends StrategyBase {

    @Override
    protected MoveStrategy internalCheck(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst) {
        return ((isFirst && newLine == 8) || (!isFirst && newLine == 1)) ? this : null;
    }

    @Override
    protected boolean checkSide(Integer currentLetter, Integer currentLine, Integer newLetter, Integer newLine, boolean isInAttack, Map<String, IChessPiece> map) {
        return super.checkSide(currentLetter, currentLine, newLetter, newLine, isInAttack, map);
    }

    @Override
    public String movement(String oldPosition, String newPosition, boolean isAttack, String... newType) {
        return super.movement(oldPosition, newPosition, isAttack)+"=" + newType[0];
    }

    @Override
    public Map<String, IChessPiece> movePiece(String newPosition, IChessPiece iPiece, ChessSide gameSide, ChessSide nextSide, String pType) {
        gameSide.getPiecesMap().remove(iPiece.getPosition());
        iPiece.move(newPosition);
        iPiece.setPieceType(CommonChessUtils.getInstance().getPieceType(pType));
        gameSide.getPiecesMap().put(newPosition, iPiece);

        Map<String, IChessPiece> map = new HashMap<String, IChessPiece>();
        map.put(iPiece.getNumber(), iPiece);
        if(nextSide.getPiecesMap().get(newPosition) != null) {
            iPiece.setInAttack(true);
            killEnemy(newPosition, gameSide, nextSide, map);
        }
        //gameSide.setPieces(CommonUtils.getBoardSideString(gameSide.getPiecesMap().values()));
        //gameSide.setLastStep(CommonUtils.getBoardSideString(map.values()));
        return map;
    }
}
