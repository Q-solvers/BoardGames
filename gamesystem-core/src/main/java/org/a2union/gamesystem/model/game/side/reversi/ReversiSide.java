package org.a2union.gamesystem.model.game.side.reversi;

import org.a2union.gamesystem.model.game.pieces.CommonReversiUtils;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.IReversiPiece;
import org.a2union.gamesystem.model.game.pieces.types.reversi.IReversiPieceType;
import org.a2union.gamesystem.model.game.side.SideBase;

/**
 * @author Iskakoff
 */
public class ReversiSide extends SideBase<IReversiPieceType, IReversiPiece>{
    @Override
    public CommonUtils getUtils() {
        return CommonReversiUtils.getInstance();
    }
}
