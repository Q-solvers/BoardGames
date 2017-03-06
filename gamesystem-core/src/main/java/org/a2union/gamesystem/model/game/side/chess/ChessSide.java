package org.a2union.gamesystem.model.game.side.chess;

import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.types.chess.King;
import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;
import org.a2union.gamesystem.model.game.side.SideBase;

import java.util.List;

/**
 * @author Iskakoff
 */
public class ChessSide extends SideBase<IChessPieceType, IChessPiece> {

    public IChessPiece getKing() {
        List<IChessPiece> kings = getPiecesTypeMap().get(new King());
        return kings.get(0);
    }

    @Override
    public CommonUtils getUtils() {
        return CommonChessUtils.getInstance();
    }
}
