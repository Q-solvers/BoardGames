/*
 * $Id: UserDetailService.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.security.authentication;

import org.a2union.gamesystem.model.user.IUserDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.User;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

/**
 * UserDetailService for work with hibernate DAO object
 *
 * @author Iskakoff
 */
public class UserDetailService implements UserDetailsService{
    private IUserDAO userDAO;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        org.a2union.gamesystem.model.user.User user = userDAO.getUserByUsername(username);
        String userRole = user.getLogin().getSuperUser() ? RolesEnum.ADMIN_ROLE.getRolename() : RolesEnum.USER_ROLE.getRolename();
        GrantedAuthorityImpl impl = new GrantedAuthorityImpl(userRole);
        return new User(user.getLogin().getUsername(), user.getLogin().getPassword(), user.getLogin().isActive(), true, true, user.getLogin().isActive(), new GrantedAuthority[]{impl});
    }

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }
}
