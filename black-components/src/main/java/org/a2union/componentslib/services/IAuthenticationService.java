/*
 * $Id: IAuthenticationService.java 187 2010-03-19 18:24:26Z iskakoff $
 */
package org.a2union.componentslib.services;

/**
 *
 * To use this service you must create your own implementation
 *
 * @author Iskakoff
 */
public interface IAuthenticationService {

    /**
     * @return true if current request contains authenticated session
     */
    boolean isAuthenticated();

    /**
     * @return true if current user has administration role
     */
    boolean isAdmin();
}
