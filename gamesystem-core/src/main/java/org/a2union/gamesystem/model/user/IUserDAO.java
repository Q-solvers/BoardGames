/*
 * $Id: IUserDAO.java 199 2010-09-28 06:40:02Z iskakoff $
 */
package org.a2union.gamesystem.model.user;

import org.a2union.gamesystem.model.base.IBaseDAO;
import org.springframework.security.userdetails.UsernameNotFoundException;

import java.util.List;

/**
 * DAO object for User
 *
 * @author Iskakoff
 */
public interface IUserDAO extends IBaseDAO<User> {
    /**
     * Find user with given name and password
     *
     * @param username name of user for check
     * @param password password for user for check
     * @return user with given name and password
     * @throws UsernameNotFoundException when there is no user with given name and password
     */
    User checkLogin(String username, String password) throws UsernameNotFoundException;

    /**
     * Obtain user by user`s name
     * @param username name of user to obtain
     * @return user with given name
     * @throws UsernameNotFoundException if there is no user with given name
     */
    User getUserByUsername(String username) throws UsernameNotFoundException;

    List<User> getOtherUsers(String username);

    List<User> getOtherUsersOrdered(String username, String order);

    User getUserByEmail(String email);

    List<User> getOtherUsersPagedOrdered(String username, String order, int startIndex, int length);
}
