package org.a2union.gamesystem.web.services;

import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;

/**
 * Service for work with http-session through Tapestry session routines
 *
 * @author Iskakoff
 */
public interface SessionService {

    GameZone getCurrentGameZone();
    String getCurrentGameZoneId();
    void setCurrentGameZone(GameZone gameZone);
    void setCurrentGameZone(GameZoneType gameZoneType);
}
