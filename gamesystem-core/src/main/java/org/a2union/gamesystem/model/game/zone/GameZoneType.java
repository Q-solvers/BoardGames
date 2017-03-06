package org.a2union.gamesystem.model.game.zone;

import org.a2union.gamesystem.model.game.pieces.CommonChessUtils;
import org.a2union.gamesystem.model.game.pieces.CommonReversiUtils;
import org.a2union.gamesystem.model.game.pieces.CommonUtils;
import org.a2union.gamesystem.model.game.side.ISide;
import org.a2union.gamesystem.model.game.side.chess.ChessSide;
import org.a2union.gamesystem.model.game.side.reversi.ReversiSide;

/**
 * @author Iskakoff
 */
public enum GameZoneType {
    CHESS_ZONE("chess", true, 2500, ChessSide.class, CommonChessUtils.getInstance()), REVERSI_ZONE("reversi", false, 0, ReversiSide.class, CommonReversiUtils.getInstance());

    private String value;
    private boolean main;
    private Integer initialRate;
    private Class<? extends ISide> sideClass;
    private CommonUtils utils;

    GameZoneType(String value, boolean main, Integer initialRate, Class<? extends ISide> aClass, CommonUtils instance) {
        this.value = value;
        this.main = main;
        this.initialRate = initialRate;
        this.sideClass = aClass;
        this.utils = instance;
    }

    public String getValue() {
        return value;
    }

    public boolean isMain() {
        return main;
    }

    public Integer getInitialRate() {
        return initialRate;
    }

    public Class<? extends ISide> getSideClass() {
        return sideClass;
    }

    public CommonUtils getUtils() {
        return utils;
    }
}
