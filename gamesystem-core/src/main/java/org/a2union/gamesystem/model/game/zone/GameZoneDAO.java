/*
 * $Id: $
 */
package org.a2union.gamesystem.model.game.zone;

import org.a2union.gamesystem.model.base.BaseDAO;

import java.util.List;

/**
 * @author Iskakoff
 */
public class GameZoneDAO extends BaseDAO<GameZone> implements IGameZoneDAO {
    public Class<GameZone> getBaseClass() {
        return GameZone.class;
    }

    public GameZone getMainZone() {
        List res = getHibernateTemplate().find("from " + getBaseClass().getName() + " bc where bc.main=?", true);
        return res.size()>0 ? (GameZone) res.get(0) : null;
    }

    public GameZone getZoneByType(GameZoneType gameZoneType) {
        List res = getHibernateTemplate().find("from " + getBaseClass().getName() + " bc where bc.type=?", gameZoneType);
        return res.size()>0 ? (GameZone) res.get(0) : null;
    }

    /**
     * init game zones
     *
     * TODO deside common strategies for default entities creation
     */
    protected void init() {
        for (GameZoneType type : GameZoneType.values()) {
            GameZone gameZone = getZoneByType(type);
            if(gameZone == null) {
                gameZone = new GameZone();
                gameZone.setType(type);
                gameZone.setMain(type.isMain());
                save(gameZone);
            }
        }
    }
}
