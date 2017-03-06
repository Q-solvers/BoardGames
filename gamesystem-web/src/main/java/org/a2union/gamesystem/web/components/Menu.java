/*
 * $Id: $
 */
package org.a2union.gamesystem.web.components;

import org.a2union.gamesystem.web.pages.Main;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * @author Iskakoff
 */
public class Menu {
    @Inject
    private Request request;

    public String getLogoutUrl() {
        return request.getContextPath() + "/j_spring_security_logout";
    }

    public Object onActionFromLogout() {
        request.getSession(false).invalidate();
        return Main.class;
    }
}
