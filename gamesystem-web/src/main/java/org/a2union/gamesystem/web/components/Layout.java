/*
 * $Id: Layout.java 181 2010-02-16 19:38:28Z iskakoff $
 */
package org.a2union.gamesystem.web.components;

import org.a2union.gamesystem.web.pages.Main;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;

/**
 * @author Iskakoff
 */
@IncludeStylesheet(value = "context:css/workbench.css")
public class Layout {

    @Inject
    @Path(value = "context:images/logo.jpg")
    private Asset logo;
    @Inject
    @Path(value = "context:images/chess-logo.jpg")
    private Asset chessLogo;
    @Inject
    @Path(value = "context:images/favicon.ico")
    private Asset icon;
    @Inject
    @Path(value = "context:images/shadow.gif")
    private Asset shadow;

    @Inject
    private Request request;

    public Asset getLogo() {
        return logo;
    }

    public void setLogo(Asset logo) {
        this.logo = logo;
    }

    public Asset getChessLogo() {
        return chessLogo;
    }

    public void setChessLogo(Asset chessLogo) {
        this.chessLogo = chessLogo;
    }

    public Asset getIcon() {
        return icon;
    }

    public void setIcon(Asset icon) {
        this.icon = icon;
    }

    public Asset getShadow() {
        return shadow;
    }

    public void setShadow(Asset shadow) {
        this.shadow = shadow;
    }

    /**
     * Clear Security context
     *
     * @return Login page
     */
    public Object onActionFromLogout() {
        request.getSession(false).invalidate();
        return Main.class;
    }
}
