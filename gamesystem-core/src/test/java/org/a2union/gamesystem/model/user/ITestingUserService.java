package org.a2union.gamesystem.model.user;

/**
 * @author Iskakoff
 */
public interface ITestingUserService extends IUserService{
    String getCurrentUserId();
    void setCurrentUserId(String currentUserId);
}
