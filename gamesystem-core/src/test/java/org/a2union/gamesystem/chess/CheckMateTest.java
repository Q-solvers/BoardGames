package org.a2union.gamesystem.chess;

import org.a2union.gamesystem.model.game.chess.IChessGameService;
import org.a2union.gamesystem.model.game.pieces.IChessPiece;
import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.Map;

/**
 * @author Iskakoff
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = false)
public class CheckMateTest {

    @Autowired
    private IChessGameService gameService;

    private static final String BLACK_SIDE =
            "type:p;number:12;position:C7;oldPosition:C7;initial:true\n"
                    + "type:p;number:13;position:D6;oldPosition:D7;initial:false\n"
                    + "type:p;number:11;position:B6;oldPosition:B7;initial:false\n"
                    + "type:p;number:10;position:A7;oldPosition:A7;initial:true\n"
                    + "type:p;number:17;position:H7;oldPosition:H7;initial:true\n"
                    + "type:p;number:16;position:G6;oldPosition:G7;initial:false\n"
                    + "type:Kp;number:118;position:G8;oldPosition:G7;initial:false\n"
                    + "type:Q;number:117;position:D8;oldPosition:D8;initial:true\n"
                    + "type:B;number:116;position:F8;oldPosition:E7;initial:false\n"
                    + "type:K;number:114;position:F6;oldPosition:G8;initial:false\n"
                    + "type:R;number:112;position:H8;oldPosition:F8;initial:false\n"
                    + "type:R;number:111;position:C8;oldPosition:A8;initial:false";

    private static final String WHITE_SIDE = "type:p;number:04;position:E4;oldPosition:E2;initial:false\n"
            + "type:p;number:05;position:F3;oldPosition:F2;initial:false\n"
            + "type:p;number:06;position:G2;oldPosition:G2;initial:true\n"
            + "type:p;number:07;position:H2;oldPosition:H2;initial:true\n"
            + "type:p;number:00;position:A3;oldPosition:A2;initial:false\n"
            + "type:p;number:02;position:B3;oldPosition:C2;initial:false\n"
            + "type:p;number:03;position:D3;oldPosition:D2;initial:false\n"
            + "type:R;number:011;position:E1;oldPosition:D1;initial:false\n"
            + "type:R;number:012;position:H1;oldPosition:H1;initial:true\n"
            + "type:Q;number:017;position:F7;oldPosition:C4;initial:false\n"
            + "type:Kp;number:018;position:C1;oldPosition:E1;initial:false\n"
            + "type:B;number:016;position:F1;oldPosition:F1;initial:true\n"
            + "type:K;number:014;position:G5;oldPosition:H3;initial:false";

    @Test
    public void checkKingAtackEnemyAtDefeatedField() {
        Map<String, IChessPiece> side = CommonChessUtils.getInstance().getBoardSide(BLACK_SIDE);
        Map<String, IChessPiece> enemySide = CommonChessUtils.getInstance().getBoardSide(WHITE_SIDE);

        if (gameService.canMoveMap(side, enemySide, "G8", false, true))
            throw new AssertionError("Can not move.");
    }
}
