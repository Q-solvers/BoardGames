package org.a2union.gamesystem.model.game.chess;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.GameService;
import org.a2union.gamesystem.model.game.GameStatus;
import org.a2union.gamesystem.model.game.GameResult;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.chessstrategies.MoveStrategy;
import org.a2union.gamesystem.model.game.pieces.types.chess.King;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;
import org.springframework.transaction.annotation.Transactional;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Date;

/**
 * @author Iskakoff
 */
public class ChessGameService extends GameService<GameBase, IChessPieceType, IChessPiece> implements IChessGameService {
    private static Logger log = Logger.getLogger(ChessGameService.class);

    @Override
    public CommonUtils<IChessPieceType, IChessPiece> getUtils() {
        return CommonChessUtils.getInstance();
    }


    public boolean canMove(GameSide gameSide) {
        return canMove((ChessSide) gameSide.getISide(), (ChessSide) gameSide.getNext().getISide(), gameSide.isFirst());
    }

    /**
     * TODO Optimize this method
     * Check for mate or stalemate
     *
     * @param gameSide - current game side
     * @param nextSide - enemy side
     * @param first - is current size is first
     * @return true if specified side can move
     */
    public boolean canMove(ChessSide gameSide, ChessSide nextSide, boolean first) {
        Map<String, IChessPiece> pieceMap = new HashMap<String, IChessPiece>();
        Map<String, IChessPiece> enemyPieceMap = new HashMap<String, IChessPiece>();
        pieceMap.putAll(gameSide.getPiecesMap());

        enemyPieceMap.putAll(nextSide.getPiecesMap());
        List<IChessPiece> kings = gameSide.getPiecesTypeMap().get(new King());
        //there are must be only one king :-)
        IPiece king = kings.get(0);
        String kingPosition = king.getPosition();
        // only one side can be first
        boolean isNextFirst = !first;

        return canMoveMap(pieceMap, enemyPieceMap, kingPosition, first, isNextFirst);
    }

    public boolean canMoveMap(Map<String, IChessPiece> pieceMap, Map<String, IChessPiece> enemyPieceMap, String kingPosition, boolean first, boolean nextFirst) {
        Set<IChessPiece> pieces = new HashSet<IChessPiece>();
        pieces.addAll(pieceMap.values());
        for (IChessPiece piece : pieces) {
            for (int i = 1; i <= 8; i++)
                for (int j = 1; j <= 8; j++) {
                    String pos = getUtils().convertLetterNumber(i) + j;
                    boolean inAttack = piece.isInAttack();
                    piece.setInAttack(enemyPieceMap.containsKey(pos));
//                    log.debug("============START=================");
                    MoveStrategy moveStrategy = (MoveStrategy) piece.checkMove(pos, pieceMap, enemyPieceMap, first);
//                    log.debug("============FINISH================");
                    if (moveStrategy != null) {
                        String position = piece.getPosition();
                        pieceMap.remove(position);
                        pieceMap.put(pos, piece);
                        IChessPiece iPiece = enemyPieceMap.remove(pos);
                        log.debug(moveStrategy);
//                        log.debug("pos:" + pos + "\tpiece:" + piece);
                        if (moveStrategy.checkForCheck(
                                new King().equals(piece.getPieceType()) ? pos : kingPosition, enemyPieceMap, pieceMap, nextFirst)) {
                            pieceMap.remove(pos);
                            piece.setPosition(position);
                            pieceMap.put(position, piece);
                            return true;
                        }
                        if (iPiece != null) {
                            enemyPieceMap.put(pos, iPiece);
                        }
                        pieceMap.remove(pos);
                        piece.setPosition(position);
                        pieceMap.put(position, piece);
                    }
                    piece.setInAttack(inAttack);
                }
        }
        return false;
    }

    @Transactional
    public Map<String, IChessPiece> doMove(GameBase game, IChessPiece piece, String newPosition) {
        return doMove(game, piece, newPosition, false, null);
    }

    @Transactional
    public Map<String, IChessPiece> doMove(GameBase game, IChessPiece piece, String newPosition, boolean exch, String ptype) {

        if (!GameStatus.ACTIVE.equals(game.getStatus())) {
            throw new IllegalStateException("Game must be active");
        }
        GameSide gameSide = getCurrentGameSide(game);
        if (!gameSide.isActive()) {
            return null;
        }
        ChessSide iSide = (ChessSide) gameSide.getISide();
        ChessSide nextISide = (ChessSide) gameSide.getNext().getISide();
        MoveStrategy moveStrategy = (MoveStrategy) piece.checkMove(newPosition, iSide.getPiecesMap(), gameSide.getNext().getISide().getPiecesMap(), gameSide.isFirst());
        if (moveStrategy == null) {
            return null;
        }
        GameSide nextSide = gameSide.getNext();
        Map<String, IChessPiece> result = moveStrategy.movePiece(newPosition, piece, iSide, nextISide, ptype);
        // TODO check for check :-)
        IPiece king = iSide.getKing();
        String kingPosition = king.getPosition();
        if (!moveStrategy.checkForCheck(kingPosition, nextISide.getPiecesMap(), iSide.getPiecesMap(), nextSide.isFirst())) {
            return null;
        }
        //TODO create toString in Strategies
        //TODO use short algebraic notation instead of
        Step step = new Step();
        step.setNumber(gameSide.getLastStepNumber());
        step.setPlayer(gameSide.getUser());
        step.setCreationTime(new Date());
        StringBuffer buffer = new StringBuffer();
        buffer.append(moveStrategy.movement(piece.getOldPosition(), piece.getPosition(), piece.isInAttack(), ptype));
        gameSide.addLastStep(getUtils().getBoardSideString(result.values()));
        gameSide.setPieces(getUtils().getBoardSideString(iSide.getPiecesIdMap().values()));
        gameSide.setActive(false);
        gameSide.setDrawProposed(false);
        nextSide.setActive(true);
        nextSide.setDrawProposed(false);
        if (piece.isInAttack() || exch) {
            nextSide.setPieces(getUtils().getBoardSideString(nextISide.getPiecesMap().values()));
        }
        king = nextISide.getKing();
        kingPosition = king.getPosition();

        boolean enemyCanMove = canMove(nextSide);
        if (!moveStrategy.checkForCheck(kingPosition, iSide.getPiecesMap(), nextISide.getPiecesMap(), gameSide.isFirst())) {
            buffer.append(enemyCanMove ? "+" : "x");
            if (!enemyCanMove) {
                game.setResult(gameSide.isFirst() ? GameResult.WHITE_WON : GameResult.BLACK_WON);
            }
        } else if (!enemyCanMove) {
            game.setResult(GameResult.DRAW);
        }

        if (!enemyCanMove) {
            completeGame(game);
        }

        step.setStepInfo(buffer.toString());
        game.addStep(step);
        notifyEnemy(game, gameSide, buffer.toString());
        return result;

    }

    private boolean pawnRestFinishLine(IChessPiece piece, String newPosition, boolean white) {
        String finishLineNumber = "1";
        if (white)
            finishLineNumber = "8";
        return "p".equals(piece.getPieceType().getType()) && StringUtils.contains(newPosition, finishLineNumber);
    }

    public boolean isPawnExchange(GameBase game, IChessPiece piece, String newPosition) {
        GameSide side = getCurrentGameSide(game);
        MoveStrategy moveStrategy = (MoveStrategy) piece.checkMove(newPosition, side.getISide().getPiecesMap(), side.getNext().getISide().getPiecesMap(), side.isFirst());
        IPiece king = ((ChessSide)side.getISide()).getKing();
        String kingPosition = king.getPosition();
        return moveStrategy != null && moveStrategy.checkForCheck(kingPosition, side.getNext().getISide().getPiecesMap(), side.getISide().getPiecesMap(), side.isFirst()) &&
                pawnRestFinishLine(piece, newPosition, side.isWhite());
    }

}
