package org.a2union.gamesystem.model.game.pieces.chessstrategies;

import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.CommonStrategy;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;

import java.util.Map;
import java.util.HashMap;

/**
 * Check for correctness of move
 *
 * @author Iskakoff
 */
public interface MoveStrategy extends CommonStrategy {
    MoveStrategy isInStrategy(String currentPosition, String newPosition,
                              Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isInAttack, boolean isFirst);
    /**
     * move piece by selected strategy
     *
     * @param newPosition - target position
     * @param iPiece      - piece
     * @param gameSide    - move side
     * @param nextSide    - enemy side
     * @return result of move contains all pieces involved in current movement with their target positions
     */
    Map<String, IChessPiece> movePiece(String newPosition, IChessPiece iPiece, ChessSide gameSide, ChessSide nextSide);

    /**
     * @param newPosition
     * @param iPiece
     * @param gameSide
     * @param nextSide
     * @param ptype
     * @return
     */
    Map<String, IChessPiece> movePiece(String newPosition, IChessPiece iPiece, ChessSide gameSide, ChessSide nextSide, String ptype);

    /**
     * check can some of enemy pieces achieve the specified position to attack
     *
     * @param kingPosition   - position of king
     * @param piecesMap      - current side pieces
     * @param enemyPiecesMap - enemy side pieces
     * @param isFirst        - is current side first
     * @return false if there are at least one piece that can attack the King
     */
    boolean checkForCheck(String kingPosition, Map<String, IChessPiece> piecesMap, Map<String, IChessPiece> enemyPiecesMap, boolean isFirst);

    /**
     * returns step string representation
     *
     * @param oldPosition - old piece position
     * @param newPosition - new piece position
     * @param isAttack    - is piece in attack
     * @param newType     - new piece type for pawn exchange strategy use only
     * @return movement string
     */
    String movement(String oldPosition, String newPosition, boolean isAttack, String... newType);
}
