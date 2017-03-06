package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.base.IBaseDAO;
import org.a2union.gamesystem.model.user.User;

import java.util.List;

public interface ITournamentDAO extends IBaseDAO<Tournament> {
    List<Tournament> getOpenTournaments(User user);
}
