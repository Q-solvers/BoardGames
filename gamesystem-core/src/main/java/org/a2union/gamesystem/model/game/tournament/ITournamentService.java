package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.base.IBaseService;
import org.a2union.gamesystem.model.user.User;

import java.util.List;
import java.util.Date;


public interface ITournamentService  extends IBaseService<Tournament> {
    Tournament createTournament(User owner, int playersCount, Date expirationDate);

    List<Tournament> getOpenTournaments(User user);
}
