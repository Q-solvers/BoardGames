/*
 * $Id: GameBase.java 142 2009-10-09 12:33:12Z iskakoff $
 */
package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author petrov
 */
@Entity
@Table(name = "tbl_game_base")
public class GameBase extends Base {

    @Column(name = "STATUS")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.GameStatus")}
    )
    private GameStatus status;

    @Column(name = "CREATION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Column(name = "START_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "END_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endTime;

    @OneToMany
    @JoinColumn(name = "HISTORY_COLUMN")
    @Cascade(CascadeType.ALL)
    private Set<Step> history = new HashSet<Step>();

    @JoinColumn(name = "ZONE_ID", nullable = false)
    @ManyToOne
    private GameZone zone;

    @Column(name = "GAME_NAME_COLUMN", length = 255)
    private String gameName;

    @OneToMany
    @JoinColumn(name = "GAME_SIDE_COLUMN")
    @Cascade(CascadeType.ALL)
    private Set<GameSide> gameSides = new HashSet<GameSide>();

    @JoinColumn(name = "OWNER_ID", nullable = false)
    @ManyToOne
    private User owner;

    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.GameResult")}
    )
    @Column(name = "RESULT_COLUMN")
    private GameResult result;

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Set<Step> getHistory() {
        return Collections.unmodifiableSet(history);
    }

    public void addStep(Step step) {
        history.add(step);
    }

    public GameZone getZone() {
        return zone;
    }

    public void setZone(GameZone zone) {
        this.zone = zone;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public Set<GameSide> getGameSides() {
        return Collections.unmodifiableSet(gameSides);
    }


    /**
     * TODO we must to override this method whrn different types of games will be used
     *
     * now we think that there are only two sides
     *
     * @param side - game side
     */
    public void addSide(GameSide side) {
        if(!gameSides.isEmpty())      {
            GameSide enemySide = gameSides.iterator().next();
            enemySide.setNext(side);
            side.setNext(enemySide);
        }
        this.gameSides.add(side);
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }

    public GameSideType getFreeType() {
        if (gameSides.size() != 1)
            return null;
        GameSide side = gameSides.iterator().next();
        if (GameSideType.FIRST.equals(side.getType()))
            return GameSideType.SECOND;
        else
            return GameSideType.FIRST;
    }

    public boolean isActive(){
        return GameStatus.ACTIVE.equals(status);
    }
}
