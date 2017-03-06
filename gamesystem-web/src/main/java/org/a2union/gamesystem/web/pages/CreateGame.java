/*
 * $Id: $
 */
package org.a2union.gamesystem.web.pages;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.web.services.SessionService;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Iskakoff
 */
public class CreateGame {

    @Inject
    @Service("gameService")
    private IGameService<GameBase, IPieceType, IPiece<IPieceType>> gameService;
    @Inject
    @Service("userService")
    private IUserService userService;
    @Inject
    private SessionService service;
    @Component(id = "createGame", type = "Form")
    private Form createForm;

    private GameSideType type;

    private GameZone gameZone;

    private String gameName;

    public void onActivate(String zoneId) {
        gameZone = gameService.getGameZone(zoneId);
    }

    public void onActivate() {
        gameZone = service.getCurrentGameZone();
        if (gameZone == null)
            gameZone = userService.getCurrentUser().getDefaultZone();
    }

    public Object onSelectedFromCancel() {
        return Index.class;
    }

    public Object onSuccess() {
        GameBase game = new GameBase();
        game.setGameName(gameName);
        game.setZone(gameZone);
        gameService.createGame(game, type, userService.getCurrentUser());
        return Index.class;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Validate("required")
    public GameSideType getType() {
        return type;
    }

    public void setType(GameSideType type) {
        this.type = type;
    }
}
