/*
 * $Id: GameStatus.java 25 2009-02-16 14:13:41Z iskakoff $
 */
package org.a2union.gamesystem.model.game;

/**
 * @author a.petrov
 *         Date: 16.02.2009 11:41:02
 */
public enum GameStatus {
    ACTIVE("active"), NOT_ACTIVE("not_active"), COMPLETED("completed");

    private String value;

    GameStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
