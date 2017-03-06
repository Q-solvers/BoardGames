package org.a2union.game;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.types.chess.Knight;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @author a.petrov
 *         Date: 04.03.2009 19:07:25
 */
public class GameBaseTest extends AbstractDependencyInjectionSpringContextTests {
    public GameBaseTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(GameBaseTest.class);
    }
    
    public void testGameBaseCreation(){
        assertTrue(true);
        String t = "type:K;position:e4;number:5";
        Knight knight = new Knight();
        Field field = ReflectionUtils.findField(Knight.class, "position");
        IPiece piece = CommonChessUtils.getInstance().getPiece(t);
        piece.getPosition();
    }
}
