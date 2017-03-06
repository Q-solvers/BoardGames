package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.model.game.pieces.types.chess.IChessPieceType;
import org.a2union.gamesystem.model.game.pieces.types.chess.King;
import org.a2union.gamesystem.model.game.pieces.types.chess.Knight;
import org.a2union.gamesystem.model.game.pieces.types.chess.Pawn;
import org.junit.Assert;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Iskakoff
 */
@RunWith(JUnit4ClassRunner.class)
public class TypeTest {
    @Test
    public void testEquals() {
        IChessPieceType pawn1 = new Pawn();
        IChessPieceType pawn2 = new Pawn();
        Assert.assertEquals(pawn1, pawn2);
    }

    @Test
    public void testNotEquals() {
        IChessPieceType pawn = new Pawn();
        IChessPieceType knight = new Knight();
        Assert.assertTrue(!pawn.equals(knight));
    }

    public void testTypeMap() {
        Map<IChessPieceType, String> map = new HashMap<IChessPieceType, String>();
        King king1 = new King();
        King king2 = new King();
        map.put(king1, "1");
        Assert.assertEquals(map.get(king2), "1");
    }
}

