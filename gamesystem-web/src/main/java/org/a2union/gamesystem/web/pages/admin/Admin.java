package org.a2union.gamesystem.web.pages.admin;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Block;
import org.a2union.gamesystem.model.user.IUserService;

/**
 * @author Iskakoff
 */
public class Admin {
    @Inject
    @Path("context:images/tabs/admin/users.gif")
    private Asset users_tab;
    @Inject
    @Path("context:images/tabs/admin/users-active.gif")
    private Asset users_tab_active;

    @Inject
    @Path("context:images/tabs/admin/tournaments.gif")
    private Asset tournaments_tab;
    @Inject
    @Path("context:images/tabs/admin/tournaments-active.gif")
    private Asset tournaments_tab_active;

    @Inject
    private IUserService userService;

    @Inject
    private Block usersBlock;

    @Inject
    private Block tournamentsBlock;

    @Inject
    private Messages messages;

    @Persist()
    private int block;

    public void onActivate() {

    }

    public void onActivate(Object id) {
        onActivate();
        block = Integer.parseInt(id.toString());
    }

    // TAB selection

    public Asset getUsers_tab() {
        return users_tab;
    }

    public Asset getUsers_tab_active() {
        return users_tab_active;
    }

    public Asset getTournaments_tab() {
        return tournaments_tab;
    }

    public Asset getTournaments_tab_active() {
        return tournaments_tab_active;
    }
//
//    public void onActionFromUsersBlock() {
//        block = 0;
//    }
//
//    public void onActionFromTournamentBlock() {
//        block = 1;
//    }

    public Object getActiveBlock() {
        if (block == 1)
            return tournamentsBlock;
        return usersBlock;
    }

    public boolean isB1() {
        return block == 0;
    }

    public boolean isB2() {
        return block == 1;
    }

    //==================================
}
