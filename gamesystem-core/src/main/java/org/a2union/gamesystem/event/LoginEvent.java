package org.a2union.gamesystem.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author Iskakoff
 */
public class LoginEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param username - name of user that currently login
     */
    public LoginEvent(String username) {
        super(username);
    }

    public String getUsername() {
        return (String) super.getSource();
    }
}
