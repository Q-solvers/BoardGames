package org.a2union.gamesystem.model.user.passreminder;

import org.a2union.gamesystem.model.base.BaseDAO;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.model.user.Login;

/**
 * @author Iskakoff
 */
public class PassReminderDAO extends BaseDAO<PassReminder> implements IPassReminderDAO {
    @Override
    public Class<PassReminder> getBaseClass() {
        return PassReminder.class;
    }
}
