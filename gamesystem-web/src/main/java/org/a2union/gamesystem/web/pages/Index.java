/*
 * $Id: Index.java 191 2010-04-02 18:10:22Z iskakoff $
 */
package org.a2union.gamesystem.web.pages;

import org.a2union.gamesystem.commons.ActiveGames;
import org.a2union.gamesystem.commons.GamesOwner;
import org.a2union.gamesystem.commons.IBoardGame;
import org.a2union.gamesystem.commons.OpenGames;
import org.a2union.gamesystem.commons.UserInvitations;
import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.invitation.GameInvitation;
import org.a2union.gamesystem.model.game.invitation.IGameInvitationService;
import org.a2union.gamesystem.model.game.side.GameSide;
import org.a2union.gamesystem.model.game.step.Step;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.web.pages.chess.ChessGame;
import org.a2union.gamesystem.web.pages.reversi.ReversiGame;
import org.a2union.gamesystem.web.services.SessionService;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.IncludeStylesheet;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Iskakoff
 */
@IncludeStylesheet(value = "context:css/gameslist.css")
public class Index {

    @Inject
    @Path("context:images/kill.gif")
    private Asset kill;

    @Inject
    @Path("context:images/stepactive.gif")
    private Asset stepactive;

    @Inject
    @Path("context:images/stepinactive.gif")
    private Asset stepinactive;

    @Inject
    @Path("context:images/tabs/active.gif")
    private Asset active;
    @Inject
    @Path("context:images/tabs/active-active.gif")
    private Asset active_active;

    @Inject
    @Path("context:images/tabs/mine.gif")
    private Asset mine;
    @Inject
    @Path("context:images/tabs/mine-active.gif")
    private Asset mine_active;

    @Inject
    @Path("context:images/tabs/opened.gif")
    private Asset opened;
    @Inject
    @Path("context:images/tabs/opened-active.gif")
    private Asset opened_active;

    @Inject
    @Path("context:images/tabs/invitations.gif")
    private Asset invitations;
    @Inject
    @Path("context:images/tabs/invitations-active.gif")
    private Asset invitations_active;


    private GamesOwner gamesOwnerDS = new GamesOwner();
    private OpenGames openGamesDS = new OpenGames();
    private ActiveGames activeGamesDS = new ActiveGames();
    private UserInvitations userInvitationsDS = new UserInvitations();
    @Inject
    private IUserService userService;
    @Inject
    @Service("gameService")
    private IGameService<GameBase, IPieceType, IPiece<IPieceType>> gameService;
    @Inject
    private IGameInvitationService gameInvitationService;

    @Inject
    private SessionService service;

    @Inject
    private Block myGames;

    @Inject
    private Block openGames;

    @Inject
    private Block activeGames;

    @Inject
    private Block gameInvitations;

    @Inject
    private Messages messages;
    @InjectPage
    private ChessGame chessGame;
    @InjectPage
    private ReversiGame reversiGame;

    private GameBase game;

    private GameInvitation gameInvitation;

    @Persist()
    private int block;

    public void onActivate() {
        setupDataSource(service.getCurrentGameZoneId());
    }

    public void onActivate(Object id) {
        onActivate();
        block = Integer.parseInt(id.toString());
    }

    // Actions

    public void onActionFromDelete(String gameId) {
        gameService.deleteGame(gameId);
    }

    public Object onActionFromAcceptOpenGame(String gameId) {
        gameService.acceptGame(gameId, userService.getCurrentUser());
        IBoardGame game = getCurrentZoneGame(gameId);
        game.setGameId(gameId);
        return game;
    }

    public Object onActionFromAcceptInvitation(String gameId) {
        GameInvitation invitation = gameInvitationService.getById(gameId);
        String s = gameInvitationService.acceptInvitation(invitation);
        IBoardGame game = getCurrentZoneGame(s);
        game.setGameId(s);
        return game;
    }

    public void onActionFromDeclineInvitation(String gameId) {
        GameInvitation invitation = gameInvitationService.getById(gameId);
        gameInvitationService.declineInvitation(invitation);
    }

    public Object onActionFromMinegames(String gameId) {
        return onActionFromOpengames(gameId);
    }

    public Object onActionFromOpengames(String gameId) {
        IBoardGame game = getCurrentZoneGame(gameId);
        game.setGameId(gameId);
        return game;
    }

    private IBoardGame getCurrentZoneGame(String gameId) {
        GameBase base = gameService.getGame(gameId);
        if(base == null)
            throw new IllegalStateException("game is incorrect");
        switch (base.getZone().getType()) {
            case REVERSI_ZONE:
                return reversiGame;
            default :
                return chessGame;
        }
    }

    // ================================================

    // Getter/Setter methods

    public GameBase getGame() {
        return game;
    }

    public void setGame(GameBase game) {
        this.game = game;
    }

    public GameInvitation getGameInvitation() {
        return gameInvitation;
    }

    public void setGameInvitation(GameInvitation gameInvitation) {
        this.gameInvitation = gameInvitation;
    }

    public String getSide() {
        return messages.format(gameService.getCurrentGameSide(getGame()).getType().toString());
    }

    public String getEnemy() {
        GameSide side = gameService.getCurrentGameSide(getGame()).getNext();
        return side != null ? side.getUser().getLogin().getUsername() : "";
    }

    public String getFreeSide() {
        return messages.format(getGame().getFreeType().toString());
    }

    public String getInvitationSide() {
        return messages.format(getGameInvitation().getInvitedUserSideType().toString());
    }

    public Asset getStepLogo() {
        GameSide side = gameService.getCurrentGameSide(getGame());
        return side.isActive() ? stepactive : stepinactive;
    }

    public String getLastStep() {
        GameSide side = gameService.getCurrentGameSide(getGame());
        Step lastStep = gameService.getLastStep(getGame());
        if(lastStep == null) {
            return "";
        }
        User player = lastStep.getPlayer();
        side = side.getUser().equals(player) ? side : side.getNext();
        return (lastStep.getNumber()) + ". " + (side.isFirst() ? lastStep.getStepInfo(): "... " + lastStep.getStepInfo());
    }

    public String getStepLogoDescr() {
        GameSide side = gameService.getCurrentGameSide(getGame());
        return side.isActive() ? messages.get("ACTIVE_SIDE") : messages.get("INACTIVE_SIDE");
    }

    // ================================================
    // Data source routines

    private void setupDataSource(String gameZoneId) {
        gamesOwnerDS.setGameZone(gameZoneId);
        gamesOwnerDS.setUser(userService.getCurrentUser());
        gamesOwnerDS.setGameService(gameService);

        openGamesDS.setGameZone(gameZoneId);
        openGamesDS.setUser(userService.getCurrentUser());
        openGamesDS.setGameService(gameService);

        activeGamesDS.setGameZone(gameZoneId);
        activeGamesDS.setUser(userService.getCurrentUser());
        activeGamesDS.setGameService(gameService);

        userInvitationsDS.setGameInvitationService(gameInvitationService, service.getCurrentGameZone());
    }

    public GamesOwner getGamesOwnerDS() {
        return gamesOwnerDS;
    }

    public void setGamesOwnerDS(GamesOwner gamesOwnerDS) {
        this.gamesOwnerDS = gamesOwnerDS;
    }

    public OpenGames getOpenGamesDS() {
        return openGamesDS;
    }

    public void setOpenGamesDS(OpenGames openGamesDS) {
        this.openGamesDS = openGamesDS;
    }

    public ActiveGames getActiveGamesDS() {
        return activeGamesDS;
    }

    public void setActiveGamesDS(ActiveGames activeGamesDS) {
        this.activeGamesDS = activeGamesDS;
    }

    public UserInvitations getUserInvitationsDS() {
        return userInvitationsDS;
    }

    public void setUserInvitationsDS(UserInvitations userInvitationsDS) {
        this.userInvitationsDS = userInvitationsDS;
    }

    // ===================================================

    public Asset getKill() {
        return kill;
    }

    // TAB selection

    public Asset getActive() {
        return active;
    }

    public Asset getActive_active() {
        return active_active;
    }

    public Asset getMine() {
        return mine;
    }

    public Asset getMine_active() {
        return mine_active;
    }

    public Asset getOpened() {
        return opened;
    }

    public Asset getOpened_active() {
        return opened_active;
    }

    public Asset getInvitations() {
        return invitations;
    }

    public Asset getInvitations_active() {
        return invitations_active;
    }

    public void onActionFromMyGame() {
        block = 0;
    }

    public void onActionFromOpenGame() {
        block = 1;
    }

    public void onActionFromActiveGame() {
        block = 2;
    }

    public void onActionFromInvitations() {
        block = 3;
    }


    public Object getActiveBlock() {
        if (block == 1)
            return openGames;
        else if (block == 2)
            return myGames;
        else if (block == 3)
            return gameInvitations;
        return activeGames;
    }

    public boolean isB1() {
        return block == 0;
    }

    public boolean isB2() {
        return block == 1;
    }

    public boolean isB3() {
        return block == 2;
    }

    public boolean isB4() {
        return block == 3;
    }

    // ===========================================
}
