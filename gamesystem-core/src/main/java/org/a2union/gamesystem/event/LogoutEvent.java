package org.a2union.gamesystem.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Iskakoff
 */
public class LogoutEvent extends ApplicationEvent{
    /**
     * Create a new LogoutEvent.
     *
     * @param username - name of user that currently logout
     */
    public LogoutEvent(String username) {
        super(username);
    }

    public String getUsername() {
        return (String) super.getSource();
    }
}
