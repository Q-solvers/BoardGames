package org.a2union.gamesystem.model.game.chess;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;

import java.util.Map;

/**
 * @author Iskakoff
 */
public interface IChessGameService extends IGameService<GameBase, IChessPieceType, IChessPiece> {
    Map<String, IChessPiece> doMove(GameBase game, IChessPiece piece, String newPosition, boolean exch, String ptype);

    boolean isPawnExchange(GameBase game, IChessPiece piece, String newPosition);
}
