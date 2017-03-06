package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.GameStatus;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.CascadeType;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

@Entity
@Table(name = "tbl_match")
public class Match extends Base {
    /*
    match number in Tournament table.
     */
    @Column(name = "NUMBER")
    private String number;

    @OneToMany
    @JoinColumn(name = "MATCH_COLUMN")
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private Set<GameBase> games = new HashSet<GameBase>();


    @ManyToMany(targetEntity = org.a2union.gamesystem.model.game.tournament.Participant.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(name = "MATCH_PARTICIPANT",
            joinColumns = @JoinColumn(name = "MATCH_ID"),
            inverseJoinColumns = @JoinColumn(name = "PART_ID")
    )
    private Set<Participant> participants = new HashSet<Participant>();

    @Column(name = "STATUS")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.GameStatus")}
    )
    private GameStatus status;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Set<GameBase> getGames() {
        return Collections.unmodifiableSet(games);
    }

    public void addGame(GameBase game) {
        games.add(game);
    }

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public Set getParticipants() {
        return Collections.unmodifiableSet(participants);
    }

    public void addParticipant(Participant participant) {
        participants.add(participant);
    }
}
