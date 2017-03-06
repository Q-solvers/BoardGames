package org.a2union.gamesystem.model.user;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface OnlineUserRegistry {
    boolean isUserOnline(String username);
    List<String> getOnlineUsers(int begin, int size);
    void regiserUser(String username);
    void unregisterUser(String username);
}
