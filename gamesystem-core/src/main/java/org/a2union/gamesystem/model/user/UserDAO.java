/*
 * $Id: UserDAO.java 209 2010-10-16 06:31:26Z iskakoff $
 */
package org.a2union.gamesystem.model.user;

import org.a2union.gamesystem.model.base.BaseDAO;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.hibernate.Query;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * @author Iskakoff
 */
public class UserDAO extends BaseDAO<User> implements IUserDAO {
    private static final String OTHER_USERS_QUERY = "from " + User.class.getName() + " u where u.login.active=:is_active and u.login.username <> :username ";
    public Class<User> getBaseClass() {
        return User.class;
    }

    @SuppressWarnings("unchecked")
    public User checkLogin(String username, String password) throws UsernameNotFoundException {
        List<User> users = getHibernateTemplate().find("from " + User.class.getName() + " u where u.login.username=? and u.login.password=? and u.login.active=?", new Object[]{username, password, true});
        if (users.size() == 0)
            throw new UsernameNotFoundException("Пользователь не найден");
        return users.get(0);
    }

    @SuppressWarnings("unchecked")
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = getHibernateTemplate().find("from " + User.class.getName() + " u where u.login.username=?", new Object[]{username});
        if (users.size() == 0)
            throw new UsernameNotFoundException("Пользователь не найден");
        return users.get(0);
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getOtherUsers(String username) {
        return getHibernateTemplate().find("from " + User.class.getName() + " u where u.login.username <> ?", new Object[]{username});
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getOtherUsersOrdered(String username, String order) {
        return getHibernateTemplate().find("from " + User.class.getName() + " u where u.login.active=? and u.login.username <> ? " +
                (StringUtils.isNotEmpty(order) ? order : "") , new Object[]{true, username});
    }

    @Override
    public User getUserByEmail(String email) {
        List res = getHibernateTemplate().find("from " + User.class.getName() + " u where u.email = ? "
                , new Object[]{email});
        return res!=null && res.size()>0 ? (User) res.get(0) : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> getOtherUsersPagedOrdered(String username, String order, int startIndex, int length) {
        Query q = getSession().createQuery(OTHER_USERS_QUERY + (StringUtils.isEmpty(order) ? "": order));
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("is_active", true);
        parameters.put("username", username);
        fillQuery(parameters, q);
        q.setFirstResult(startIndex);
        q.setMaxResults(length);
        return (List<User>) q.list();
    }

    @Override
    public int countObjects() {
        List result = getHibernateTemplate().find("select count(*) from " + getBaseClass().getName() + " bc where bc.login.active=?", new Object[]{true});
        return ((Number)result.get(0)).intValue();
    }
}
