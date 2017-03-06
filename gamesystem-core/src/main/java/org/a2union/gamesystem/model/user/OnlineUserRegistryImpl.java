package org.a2union.gamesystem.model.user;

import org.a2union.gamesystem.event.LoginEvent;
import org.a2union.gamesystem.event.LogoutEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Iskakoff
 */
public class OnlineUserRegistryImpl implements OnlineUserRegistry, ApplicationListener {

    private List<String> onlineUsers = Collections.synchronizedList(new ArrayList<String>());

    @Override
    public boolean isUserOnline(String username) {
        return onlineUsers.contains(username);
    }

    @Override
    public List<String> getOnlineUsers(int begin, int size) {
        if(begin >= onlineUsers.size()) {
            return new ArrayList<String>();
        } else if((begin + size) > onlineUsers.size() ) {
            return onlineUsers.subList(begin, onlineUsers.size());
        }
        return onlineUsers.subList(begin, begin+size);
    }

    @Override
    public void regiserUser(String username) {
        onlineUsers.add(username);
    }

    @Override
    public void unregisterUser(String username) {
        onlineUsers.remove(username);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof LoginEvent) {
            LoginEvent loginEvent = (LoginEvent) event;
            regiserUser(loginEvent.getUsername());
        } else if(event instanceof LogoutEvent) {
            LogoutEvent logoutEvent = (LogoutEvent) event;
            unregisterUser(logoutEvent.getUsername());
        }
    }
}
