package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;

/**
 * @author Iskakoff
 */
public class CommonChessUtils extends CommonUtils<IChessPieceType, IChessPiece> {

    private static final CommonChessUtils instanse = new CommonChessUtils();

    public static CommonChessUtils getInstance() {
        return instanse;
    }

    public CommonChessUtils() {
        super(ChessPiece.class);
    }
}
