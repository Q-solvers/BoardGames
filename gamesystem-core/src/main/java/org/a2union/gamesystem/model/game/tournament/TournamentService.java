package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.user.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Date;


public class TournamentService implements ITournamentService {
    @Autowired
    ITournamentDAO tournamentDAO;

    public Tournament createTournament(User owner, int playersCount, Date expirationDate) {
        Tournament tournament = new Tournament();
        tournament.setOwner(owner);
        tournament.setPlayersCount(playersCount);
        tournament.setStatus(TournamentStatus.OPEN);
        tournament.setCreation(new Date());
        tournament.setExpiration(expirationDate);
        tournamentDAO.save(tournament);
        return tournament;
    }

    public List<Tournament> getOpenTournaments(User user) {
        return tournamentDAO.getOpenTournaments(user);
    }

    public Tournament getById(String id) {
        return tournamentDAO.getById(id);
    }

    public String save(Tournament obj) {
        return tournamentDAO.save(obj);
    }

    public void update(Tournament obj) {
        tournamentDAO.update(obj);
    }

    public void delete(Tournament obj) {
        tournamentDAO.delete(obj);
    }
}
