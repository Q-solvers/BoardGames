/*
 * $Id: Step.java 99 2009-08-02 07:38:53Z iskakoff $
 */
package org.a2union.gamesystem.model.game.step;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @author a.petrov
 *         Date: 16.02.2009 11:47:02
 */
@Entity
@Table(name = "tbl_step")
public class Step extends Base {
    @Column(name = "CREATION_TIME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationTime;

    @Column(name = "NUMBER")
    private int number;

    @Column(name = "STEP_INFO_COLUMN")
    private String stepInfo;
    
    @Transient
    private IStepDetails details;

    /*
     stepInfo owner
    */
    @JoinColumn(name = "PLAYER_ID", nullable = false)
    @ManyToOne
    private User player;

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public IStepDetails getDetails() {
        return details;
    }

    public void setDetails(IStepDetails details) {
        this.details = details;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public String getStepInfo() {
        return stepInfo;
    }

    public void setStepInfo(String stepInfo) {
        this.stepInfo = stepInfo;
    }
}
