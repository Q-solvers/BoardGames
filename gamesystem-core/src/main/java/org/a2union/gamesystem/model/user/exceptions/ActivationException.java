/*
 * $Id: ActivationException.java 11 2009-01-15 23:54:04Z iskakoff $
 */
package org.a2union.gamesystem.model.user.exceptions;

/**
 * Exception that may happens during user activation process
 *
 * @author Iskakoff
 */
public class ActivationException extends Exception {
    public ActivationException() {
        super();
    }

    public ActivationException(String message) {
        super(message);
    }

    public ActivationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ActivationException(Throwable cause) {
        super(cause);
    }
}
