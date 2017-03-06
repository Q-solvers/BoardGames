/*
 * $Id: RolesEnum.java 22 2009-02-14 20:29:08Z iskakoff $
 */
package org.a2union.gamesystem.security.authentication;

/**
 * Available user roles enum
 *
 * @author Iskakoff
 */
public enum RolesEnum {

    ADMIN_ROLE("ROLE_ADMIN"), USER_ROLE("ROLE_USER");

    private String rolename;

    RolesEnum(String rolename) {
        this.rolename = rolename;
    }

    public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }
}
