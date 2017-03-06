/*
 * $Id: ActivationDAO.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.model.activation;

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
public class ActivationDAO extends BaseDAO<Activation> implements IActivationDAO {
    public Class<Activation> getBaseClass() {
        return Activation.class;
    }

    public Activation getActivationByUser(String userId) {
        List list = getHibernateTemplate().find(
                "from " + Activation.class.getName() + " a where a.person.UUID=? ",
                new Object[]{userId});
        if(list.size()!=0) {
            return (Activation) list.get(0);
        }
        return null;
    }

    public Activation getActivationByUserAndCode(String userId, String activationCode) {
        List list = getHibernateTemplate().find(
                "from " + Activation.class.getName() + " a where a.person.UUID=? and a.person.login.active=? and a.activationCode=?",
                new Object[]{userId, false, activationCode});
        if(list.size()!=0) {
            return (Activation) list.get(0);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Activation> getNonActiveActivations(final int pageSize) {
        return (List) getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException, SQLException {
                Query query = session.createQuery("from " + Activation.class.getName() +
                        " a where a.person.login.active=:active and a.notified=:notified ");
                query.setMaxResults(pageSize);
                query.setParameter("active", false);
                query.setParameter("notified", false);
                return query.list();
            }
        });
    }
}
