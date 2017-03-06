/*
 * $Id: $
 */
package org.a2union.gamesystem.security.authentication;

/**
 * @author Iskakoff
 */
public enum AuthenticationConstants {
    LOGIN_PAGE("LOGIN_PAGE_URL"), ERROR_PARAMETER("error") ;

    private AuthenticationConstants(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
