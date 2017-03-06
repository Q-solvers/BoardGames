/*
 * $Id: BaseDAO.java 199 2010-09-28 06:40:02Z iskakoff $
 */
package org.a2union.gamesystem.model.base;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Base DAO object
 *
 * @author Iskakoff
 */
public abstract class BaseDAO<T extends Base> extends HibernateDaoSupport implements IBaseDAO<T> {
    @SuppressWarnings("unchecked")
    public T getById(String id) {
        return (T) getHibernateTemplate().get(getBaseClass(), id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public String save(T obj) {
        getHibernateTemplate().save(obj);
        return obj.getUUID();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void update(T obj) {
        getHibernateTemplate().update(obj);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(T obj) {
        getHibernateTemplate().delete(obj);
    }

    public abstract Class<T> getBaseClass();

    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return getHibernateTemplate().loadAll(getBaseClass());
    }

    @SuppressWarnings("unchecked")
    public List<T> getPage(final int begin, final int size) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {

            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from " + getBaseClass().getName() + " bc");
                query.setFirstResult(begin);
                query.setMaxResults(size);
                return query.list();
            }
        });
    }

    @Override
    public int countObjects() {
        List result = getHibernateTemplate().find("select count(*) from " + getBaseClass().getName() + " bc");
        return ((Number)result.get(0)).intValue();
    }


    protected void fillQuery(Map<String, Object> parameters, Query q) {
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            q.setParameter(entry.getKey(), entry.getValue());
        }
    }
}
