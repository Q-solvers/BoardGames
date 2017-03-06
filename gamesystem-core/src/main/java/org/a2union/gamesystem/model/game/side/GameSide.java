/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game.side;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_game_side",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"USER_ID", "GAME_SIDE_COLUMN"})})
public class GameSide extends Base {
    /**
     * user of side
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @Column(name = "SIDE_TYPE")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.side.GameSideType")}
    )
    private GameSideType type;

    @Column(name = "PIECES_COLUMN", length = 4000)
    private String pieces;

    @Column(name = "ACTIVE_COLUMN")
    private boolean active;

    @Column(name = "LAST_STEP_COLUMN")
    private String lastStep;

    @Column(name = "LAST_STEP_NUMBER_COLUMN")
    private int lastStepNumber;

    @Column(name = "DRAW_PROPOSED_COLUMN")
    private boolean drawProposed;

    /**
     * implemetation of directional list
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "NEXT_SIDE_ID", nullable = true)
    private GameSide next;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "GAME_SIDE_COLUMN", nullable = true)
    private GameBase game;

    @Transient
    private ISide iSide;

    public ISide getISide() {
        if (iSide == null) {
            Class<? extends ISide> sideClass = getGame().getZone().getType().getSideClass();
            try {
                iSide = sideClass.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        iSide.init(pieces, isFirst());
        return iSide;
    }

    public void setISide(ISide iSide) {
        this.iSide = iSide;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isBlack() {
        return GameSideType.SECOND.equals(getType());
    }

    public boolean isWhite() {
        return GameSideType.FIRST.equals(getType());
    }

    public GameSideType getType() {
        return type;
    }

    public void setType(GameSideType type) {
        this.type = type;
    }

    public GameSide getNext() {
        return next;
    }

    public void setNext(GameSide next) {
        this.next = next;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getLastStep() {
        return lastStep;
    }

    public void setLastStep(String lastStep) {
        this.lastStep = lastStep;
    }

    public int getLastStepNumber() {
        return lastStepNumber;
    }

    public void setLastStepNumber(int lastStepNumber) {
        this.lastStepNumber = lastStepNumber;
    }

    public void addLastStep(String step) {
        setLastStep(step);
        lastStepNumber++;
    }

    public boolean isDrawProposed() {
        return drawProposed;
    }

    public void setDrawProposed(boolean drawProposed) {
        this.drawProposed = drawProposed;
    }

    public boolean isFirst() {
        return GameSideType.FIRST.equals(getType());
    }

    public GameBase getGame() {
        return game;
    }
}
