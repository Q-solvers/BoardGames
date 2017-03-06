package org.a2union.gamesystem.model.user;

import org.junit.Ignore;

/**
 * @author Iskakoff
 */
@Ignore
public class TestingUserService extends UserServiceImpl implements ITestingUserService {

    private String currentUserId = "test1";

    @Override
    public User getCurrentUser() {
        return getByUsername(currentUserId);
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }
}
