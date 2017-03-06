package org.a2union.gamesystem.model.game.tournament;

import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.model.base.BaseDAO;

import java.util.List;


public class TournamentDAO extends BaseDAO<Tournament> implements ITournamentDAO {
    public Class<Tournament> getBaseClass() {
        return Tournament.class;
    }


    public List<Tournament> getOpenTournaments(User user) {
        String hql = "select t from " + getBaseClass().getName() + " t join t.participants p where p.user.UUID=? and t.status=?";
        return getHibernateTemplate().find(hql, new Object[]{user.getUUID(), TournamentStatus.OPEN});
    }
}
