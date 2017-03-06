package org.a2union.gamesystem.web.services;

import org.a2union.gamesystem.model.game.GameBase;
import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.pieces.IPiece;
import org.a2union.gamesystem.model.game.pieces.types.IPieceType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.user.IUserService;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;

/**
 * @author Iskakoff
 */
public class SessionServiceImpl implements SessionService {

    private static final String CURRENT_GAME_ZONE_ID = "CURRENT_GAME_ZONE_ID";

    @Inject
    @Service("gameService")
    private IGameService<GameBase, IPieceType, IPiece<IPieceType>> gameService;
    @Inject
    @Service("userService")
    private IUserService userService;
    @Inject
    private RequestGlobals requestGlobals;

    public GameZone getCurrentGameZone() {
        String id = getCurrentZoneId();
        if(null == id){
            GameZone zone = userService.getCurrentUser().getDefaultZone();
            if(zone!=null)
                setCurrentGameZone(zone);
            return zone;
        }
        return gameService.getGameZone(id);
    }

    public String getCurrentGameZoneId() {
        String id = getCurrentZoneId();
        return id != null ? id : getCurrentGameZone().getUUID();
    }

    public void setCurrentGameZone(GameZone gameZone) {
        Session session = getSession();
        if(session!=null) {
            session.setAttribute(CURRENT_GAME_ZONE_ID, gameZone.getUUID());
        }

    }

    public void setCurrentGameZone(GameZoneType gameZoneType) {
        GameZone zone = gameService.obtainGameZoneByType(gameZoneType);
        setCurrentGameZone(zone);
    }

    private String getCurrentZoneId() {
        Session session = getSession();
        return session!=null ? (String) session.getAttribute(CURRENT_GAME_ZONE_ID) : null;
    }

    private Session getSession() {
        return requestGlobals.getRequest().getSession(false);
    }
}
