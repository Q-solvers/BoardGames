package org.a2union.gamesystem.web.pages;

import org.a2union.gamesystem.commons.UsersDataSource;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.invitation.IGameInvitationService;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.OnlineUserRegistry;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.web.services.SessionService;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Iskakoff
 */
@IncludeStylesheet(value = "context:css/playerslist.css")
public class Players {

    @Inject
    @Path("context:images/userinfo.gif")
    private Asset userinfo;
    @Inject
    @Path("context:images/userinfo_off.gif")
    private Asset userinfo_off;

    @Inject
    @Path("context:images/tabs/players.gif")
    private Asset players_tab;
    @Inject
    @Path("context:images/tabs/players-active.gif")
    private Asset players_tab_active;

    @Inject
    private Block players;

//    @Component(id = "inviteDialog")
//    private Dialog inviteForm;

    @Validate("required")
    private GameSideType type;

    @Inject
    @Service("onlineUserRegistry")
    private OnlineUserRegistry userRegistry;

    @Inject
    @Service("userService")
    private IUserService userService;

    @Inject
    @Service("gameService")
    private IGameService gameService;

    @Inject
    @Service("gameInvitationService")
    private IGameInvitationService gameInvitationService;

    @Inject
    private SessionService sessionService;

    private UsersDataSource playersDS = new UsersDataSource();

    private User player;

    public Asset getPlayers_tab() {
        return players_tab;
    }

    public Asset getPlayers_tab_active() {
        return players_tab_active;
    }

    public Object getBlock() {
        return players;
    }

    public Asset getLogo() {
        return userRegistry.isUserOnline(getPlayer().getLogin().getUsername()) ? userinfo : userinfo_off;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public UsersDataSource getPlayersDS() {
        return playersDS;
    }

    public void onActivate() {
        User user = userService.getCurrentUser();
        playersDS.setUserService(userService);
        playersDS.setUsername(user.getLogin().getUsername());
    }

    public GameSideType getType() {
        return type;
    }

    public void setType(GameSideType type) {
        this.type = type;
    }

    void setupRender() {
        setType(GameSideType.FIRST);
    }

    public Integer getRate() {
        return userService.getUserRateValue(player, sessionService.getCurrentGameZone());
    }

    @OnEvent(value = EventConstants.SUCCESS)
    @Transactional(propagation = Propagation.REQUIRED)
    public void success(Object context) {
        User invitedUser = userService.getByUsername((String) context);
        GameZone zone = sessionService.getCurrentGameZone();
        GameSideType mySide;
        for (GameSideType side : GameSideType.values()) {
            if(!side.equals(getType())) {
                mySide = side;
                gameInvitationService.inviteUser(invitedUser, zone, mySide, getType());
                return;
            }
        }
    }
}
