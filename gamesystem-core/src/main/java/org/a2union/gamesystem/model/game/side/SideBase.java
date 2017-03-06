package org.a2union.gamesystem.model.game.side;

import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Iskakoff
 */
public abstract class SideBase<I extends IPieceType,J extends IPiece<I>> implements ISide<I, J> {

    private Map<String, J> piecesMap = new HashMap<String,J>();

    private Map<String, J> piecesIdMap = new HashMap<String,J>();

    private Map<I, List<J>> piecesTypeMap = new HashMap<I,List<J>>();

    private String pieces;
    private boolean inited;
    private boolean first;

    public void init(String pieces, boolean first) {
        if(!inited) {
            getUtils().getBoard(pieces, piecesIdMap, piecesMap, piecesTypeMap);
            this.first = first;
            inited = true;
        }
    }

    public abstract CommonUtils getUtils();


    public Map<String, J> getPiecesMap() {
        return piecesMap;
    }

    public Map<String, J> getPiecesIdMap() {
        return piecesIdMap;
    }

    public Map<I, List<J>> getPiecesTypeMap() {
        return piecesTypeMap;
    }

    @Override
    public boolean isFirst() {
        return first;
    }

}
