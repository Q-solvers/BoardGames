package org.a2union.gamesystem.model.user.rate;

import org.a2union.gamesystem.model.base.IBaseDAO;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.user.User;

/**
 * @author Iskakoff
 */
public interface IRateDAO extends IBaseDAO<Rate> {
    Rate getRateByUserAndZone(User user, GameZone gameZone);

    Rate getRateByUserAndZone(User user, GameZoneType gameZoneType);
}
