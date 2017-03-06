package org.a2union.gamesystem.model.game.reversi;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.GameService;
import org.a2union.gamesystem.model.game.GameStatus;
import org.a2union.gamesystem.model.game.GameResult;
import org.a2union.gamesystem.model.game.pieces.CommonReversiUtils;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.IReversiPiece;
import org.a2union.gamesystem.model.game.pieces.ReversiPiece;
import org.a2union.gamesystem.model.game.pieces.ReversiStrategy;
import org.a2union.gamesystem.model.game.pieces.IReversiStrategy;
import org.a2union.gamesystem.model.game.pieces.types.reversi.IReversiPieceType;
import org.a2union.gamesystem.model.game.pieces.types.reversi.ReversiPieceType;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.reversi.ReversiSide;
import org.a2union.gamesystem.model.game.step.Step;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Iskakoff
 */
public class ReversiGameService extends GameService<GameBase, IReversiPieceType, IReversiPiece> implements IReversiGameService {

    @Override
    public CommonUtils<IReversiPieceType, IReversiPiece> getUtils() {
        return CommonReversiUtils.getInstance();
    }

    public boolean canMove(GameSide gameSide) {
        return canMove((ReversiSide) gameSide.getISide(), (ReversiSide) gameSide.getNext().getISide(), gameSide.isFirst());
    }

    private boolean canMove(ReversiSide side, ReversiSide enemySide, boolean first) {
        Set<String> freePosition = new HashSet<String>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                freePosition.add(getUtils().convertLetterNumber(i) + j);
            }
        }
        freePosition.removeAll(side.getPiecesMap().keySet());
        freePosition.removeAll(enemySide.getPiecesMap().keySet());
        ReversiPiece piece = new ReversiPiece();
        piece.setNumber("0");
        piece.setPieceType(new ReversiPieceType());
        for (String pos : freePosition) {
            piece.setPosition(pos);
            IReversiStrategy strategy = piece.checkMove(pos, side.getPiecesMap(), enemySide.getPiecesMap(), first);
            if (strategy != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, IReversiPiece> doMove(GameBase game, IReversiPiece piece, String newPosition) {
        if (!GameStatus.ACTIVE.equals(game.getStatus())) {
            throw new IllegalStateException("Game must be active");
        }
        GameSide side = getCurrentGameSide(game);
        if (!side.isActive()) {
            return null;
        }
        GameSide next = side.getNext();
        Map piecesMap = side.getISide().getPiecesMap();
        Map enemyMap = next.getISide().getPiecesMap();
        //we can move only to free position
        if (piecesMap.containsKey(newPosition) || enemyMap.containsKey(newPosition)) {
            return null;
        }
        //are there any available movements
        ReversiStrategy moveStrategy = (ReversiStrategy) piece.checkMove(newPosition, piecesMap, enemyMap, side.isFirst());
        if (moveStrategy == null) {
            return null;
        }
        Map<String, IReversiPiece> map = moveStrategy.getReversiPieceMap();
        piecesMap.putAll(map);
        for (String position : map.keySet()) {
            enemyMap.remove(position);
        }
        boolean canEnemyMove = canMove(next);
        boolean canMove = false;
        if (!canEnemyMove) {
            canMove = canMove(side);
            if (!canMove) {
                GameResult gR = enemyMap.size() > piecesMap.size() ? (side.isFirst() ? GameResult.BLACK_WON : GameResult.WHITE_WON)
                        : piecesMap.size() > enemyMap.size() ? (side.isFirst() ? GameResult.WHITE_WON : GameResult.BLACK_WON)
                        : GameResult.DRAW;
                game.setResult(gR);
                completeGame(game);
            }
        }
        side.setPieces(getUtils().getBoardSideString(piecesMap.values()));
        next.setPieces(getUtils().getBoardSideString(enemyMap.values()));
        if (canEnemyMove) {
            next.setActive(true);
            side.setActive(false);
        } else if (canMove) {
            side.setActive(true);
        }
        side.setDrawProposed(false);
        next.setDrawProposed(false);
        Step step = new Step();
        step.setNumber(side.getLastStepNumber());
        step.setPlayer(side.getUser());
        step.setCreationTime(new Date());
        step.setStepInfo(newPosition);
        side.addLastStep(CommonReversiUtils.getInstance().getBoardSideString(map.values()));
        game.addStep(step);
        if (!canEnemyMove && canMove) {
            Step nstep = new Step();
            nstep.setNumber(next.getLastStepNumber());
            nstep.setPlayer(next.getUser());
            nstep.setCreationTime(new Date());
            nstep.setStepInfo("PASS");
            next.addLastStep("");
            game.addStep(nstep);
        }
        notifyEnemy(game, side, newPosition);
        return piecesMap;
    }
}
