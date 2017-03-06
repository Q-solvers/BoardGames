package org.a2union.gamesystem.model.game.tournament;

/**
 * tournament has four statuses. This enum declare it.
 */
public enum TournamentStatus {
    /**
     * открыт
     */
    OPEN("open"),

    /**
     * проходит
     */
    CONTINUED("continued"),
    /**
     * окончен
     */
    COMPLETED("completed"),
    /**
     * отменен
     */
    CANCELLED("cancelled");

    private String value;

    TournamentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
