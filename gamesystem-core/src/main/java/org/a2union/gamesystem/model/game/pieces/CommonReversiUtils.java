package org.a2union.gamesystem.model.game.pieces;

import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.pieces.types.reversi.IReversiPieceType;
import org.a2union.gamesystem.model.game.pieces.types.reversi.ReversiPieceType;

/**
 * @author Iskakoff
 */
public class CommonReversiUtils extends CommonUtils<IReversiPieceType, IReversiPiece> {

    private static final CommonReversiUtils instanse = new CommonReversiUtils();

    public static CommonReversiUtils getInstance() {
        return instanse;
    }

    public CommonReversiUtils() {
        super(ReversiPiece.class);
    }

    @Override
    public String getInitialBoard(boolean white) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("type:RR;number:001;position:E").append(white ? 5 : 4);
        buffer.append("\n");
        buffer.append("type:RR;number:002;position:D").append(white ? 4 : 5);
        return buffer.toString();
    }

    @Override
    public IReversiPieceType getPieceType(String type) {
        return new ReversiPieceType();
    }
}
