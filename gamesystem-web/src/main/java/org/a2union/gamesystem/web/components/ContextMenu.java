/*
 * $Id: ContextMenu.java 182 2010-02-19 21:10:27Z iskakoff $
 */
package org.a2union.gamesystem.web.components;

import org.a2union.gamesystem.model.game.invitation.IGameInvitationService;
import org.a2union.gamesystem.model.user.OnlineUserRegistry;
import org.a2union.gamesystem.web.services.SessionService;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Iskakoff
 */
public class ContextMenu {
    @Inject
    @Path(value = "context:images/userinfo.gif")
    private Asset userinfo;
    @Inject
    @Service("onlineUserRegistry")
    private OnlineUserRegistry onlineUserRegistry;
    @Inject
    private SessionService service;
    @Inject
    @Service("gameInvitationService")
    private IGameInvitationService gameInvitationService;

    private Object principal;


    public Object[] getOnlineUsers() {
        return onlineUserRegistry.getOnlineUsers(0, 10).toArray();
    }

    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public Asset getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(Asset userinfo) {
        this.userinfo = userinfo;
    }

    public int getInvitesCount() {
        return gameInvitationService.countUserInvites(service.getCurrentGameZone());
    }
}
