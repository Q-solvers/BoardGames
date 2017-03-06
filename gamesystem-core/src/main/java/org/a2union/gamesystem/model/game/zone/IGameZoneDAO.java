/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game.zone;

import org.a2union.gamesystem.model.base.IBaseDAO;

/**
 * @author Iskakoff
 */
public interface IGameZoneDAO extends IBaseDAO<GameZone> {
    GameZone getMainZone();

    GameZone getZoneByType(GameZoneType gameZoneType);
}
