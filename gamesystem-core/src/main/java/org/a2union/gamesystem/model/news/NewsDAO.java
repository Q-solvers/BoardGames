/*
 * $Id: NewsDAO.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.model.news;

import org.a2union.gamesystem.model.base.BaseDAO;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Iskakoff
 */
public class NewsDAO extends BaseDAO<Article> implements INewsDAO {
    private static final int FRESH_NEWS_COUNT = 2;

    public Class<Article> getBaseClass() {
        return Article.class;
    }

    @SuppressWarnings("unchecked")
    public List<Article> listFreshNews() {
        return (List<Article>) getHibernateTemplate().execute(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from " + Article.class.getName() + " a order by a.date desc");
                query.setMaxResults(FRESH_NEWS_COUNT);
                return query.list();
            }
        });
    }
}
