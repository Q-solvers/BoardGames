package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.user.User;

import javax.persistence.*;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/**
 * Participant is  entity which presents User in Tournament. User take a part in tournament as a Participant
 * Participant take a part in matches.
 */
@Entity
@Table(name = "tbl_participant")
public class Participant extends Base {

    @JoinColumn(name = "TOURNAMENT_ID", nullable = false)
    @ManyToOne
    private Tournament tournament;

    @JoinColumn(name = "USER_ID", nullable = false)
    @ManyToOne
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "participants",
            targetEntity = Match.class
    )
    private Set<Match> matches = new HashSet<Match>();

    public Tournament getTournament() {
        return tournament;
    }

    public void setTournament(Tournament tournament) {
        this.tournament = tournament;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Match> getMatches() {
        return Collections.unmodifiableSet(matches);
    }
}
