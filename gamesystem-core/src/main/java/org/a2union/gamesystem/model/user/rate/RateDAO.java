package org.a2union.gamesystem.model.user.rate;

import org.a2union.gamesystem.model.base.BaseDAO;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.user.User;

import java.util.List;

/**
 * @author Iskakoff
 */
public class RateDAO extends BaseDAO<Rate> implements IRateDAO {
    @Override
    public Class<Rate> getBaseClass() {
        return Rate.class;
    }

    @Override
    public Rate getRateByUserAndZone(User user, GameZone gameZone) {
        return getRateByUserAndZone(user, gameZone.getType());
    }

    @Override
    public Rate getRateByUserAndZone(User user, GameZoneType gameZoneType) {
        List result = getHibernateTemplate().find("from " + Rate.class.getName() + " r where r.user=? and r.zoneType=?", new Object[]{user, gameZoneType});
        return result.size() > 0 ? (Rate) result.get(0) : null;
    }
}
