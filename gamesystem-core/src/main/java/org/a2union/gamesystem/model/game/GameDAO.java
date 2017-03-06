/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game;

import org.a2union.gamesystem.model.base.BaseDAO;
import org.a2union.gamesystem.model.game.step.Step;
import org.hibernate.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Iskakoff
 */
public class GameDAO<T extends GameBase> extends BaseDAO<T> implements IGameDAO<T> {

    @SuppressWarnings("unchecked")
    public Class<T> getBaseClass() {
        return (Class<T>) GameBase.class;
    }

    public int countItems(Map<String, Object> parameters, String query) {
        Query q = getSession().createQuery("select count(*)" + query);
        fillQuery(parameters, q);
        return ((Long) q.uniqueResult()).intValue();
    }

    @SuppressWarnings("unchecked")
    public List<T> getItems(Map<String, Object> parameters, String query) {
        Query q = getSession().createQuery(query);
        fillQuery(parameters, q);
        return (List<T>) q.list();
    }

    @SuppressWarnings("unchecked")
    public List<T> getItemsPage(Map<String, Object> parameters, String query, int startIndex, int size) {
        Query q = getSession().createQuery(query);
        fillQuery(parameters, q);
        q.setFirstResult(startIndex);
        q.setMaxResults(size);
        return (List<T>) q.list();
    }

    @Override
    public Step getLastStepByGame(GameBase game) {
        Query query = getSession().createQuery("select step from  " + getBaseClass().getName() + " g inner join g.history as step " +
                " inner join g.history as step2 where g = :game group by step.UUID, step.creationTime, step.number, step.stepInfo, step.player " +
                " having step.creationTime=max(step2.creationTime)");
        query.setParameter("game", game);
        List res = query.list();
        return res.size()>0 ? (Step) res.get(0) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Step> getAllSteps(T game) {
        Query query = getSession().createQuery("select step from  " + getBaseClass().getName() + " g inner join g.history as step " +
                " where g = :game order by step.creationTime asc");
        query.setParameter("game", game);
        return query.list();
    }
}
