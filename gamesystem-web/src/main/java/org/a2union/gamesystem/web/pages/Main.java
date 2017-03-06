/*
 * $Id: Main.java 47 2009-03-25 07:47:13Z iskakoff $
 */
package org.a2union.gamesystem.web.pages;

/**
 * @author Iskakoff
 */

public class Main {

    private boolean failed;

    /**
     * set failed parameter from page context
     *
     * @param extra parameter of login page
     */
    public void onActivate(String extra) {
        if (extra.equals("failed")) {
            failed = true;
        }
    }

    /**
     * @return true if there is error while user try to log in
     */
    public boolean isFailed() {
        return failed;
    }
}