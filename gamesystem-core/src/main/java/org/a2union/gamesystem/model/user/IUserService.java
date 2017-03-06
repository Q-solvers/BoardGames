/*
 * $Id: IUserService.java 199 2010-09-28 06:40:02Z iskakoff $
 */
package org.a2union.gamesystem.model.user;

import org.a2union.gamesystem.model.base.IBaseService;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.rate.Rate;
import org.a2union.gamesystem.model.user.passreminder.PassReminder;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface IUserService extends IBaseService<User> {
    /**
     * Validate username and password
     * 
     * @param username user`s name to validate
     * @param password user`s password to validate
     * @return if username and password are matched
     */
    boolean checkForPassword(String username, String password);

    /**
     * Get user by username
     * @param username name of user to obtain
     * @return user with given name
     */
    User getByUsername(String username);

    /**
     *
     * @return
     */
    Object[] getOnlineUsers();

    /**
     *
     * @return user of current request
     */
    User getCurrentUser();

    int usersCount();

    List<User> getOtherUsers(String username);

    List<User> getOtherUsersOrdered(String username, String order);

    Integer getUserRateValue(User player, GameZone gameZone);

    Rate getUserRate(User player, GameZone gameZone);

    boolean isUserOnline(User user);

    void updateUserDetails(User person, String password, boolean movement, boolean invitation);

    void restorePassword(String passId, String password);

    boolean remindPassword(String email);

    PassReminder geyPasswordReminder(String passId);

    boolean isUserExist(String email);

    List<User> getOtherUsersPagedOrdered(String username, String s, int startIndex, int i);
}
