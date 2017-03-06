package org.a2union.gamesystem.model.game.side;

import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;

import java.util.Map;
import java.util.List;

/**
 * @author Iskakoff
 */
public interface ISide<I extends IPieceType,J extends IPiece<I>> {
    /**
     * Initialization of board side objects
     * @param pieces string representation of board side
     */
    void init(String pieces, boolean first);
    Map<String, J> getPiecesMap();
    Map<String, J> getPiecesIdMap();
    Map<I, List<J>> getPiecesTypeMap();
    boolean isFirst();
}
