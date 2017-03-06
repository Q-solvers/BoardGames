package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Date;


@Entity
@Table(name = "tbl_tournament")
public class Tournament extends Base {


    @JoinColumn(name = "OWNER_ID", nullable = false)
    @ManyToOne
    private User owner;
    /**
     * count of players is spesified by  owner during tournament creation
     */
    @Column(name = "PLAYERS_COUNT")
    private int playersCount;

    @Column(name = "STATUS")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.tournament.TournamentStatus")}
    )
    private TournamentStatus status;

    @OneToMany(mappedBy = "tournament")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<Participant> participants = new HashSet<Participant>();

    @Column(name="CREATION")
    private Date creation;
    @Column(name="EXPIRATION")
    private Date expiration;
    @Transient
    private ITournamentTable table;

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getPlayersCount() {
        return playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }

    public TournamentStatus getStatus() {
        return status;
    }

    public void setStatus(TournamentStatus status) {
        this.status = status;
    }

    public Set<Participant> getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }

    public Date getCreation() {
        return creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }
}
