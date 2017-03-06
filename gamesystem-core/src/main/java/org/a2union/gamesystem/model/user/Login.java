/*
 * $Id: Login.java 187 2010-03-19 18:24:26Z iskakoff $
 */
package org.a2union.gamesystem.model.user;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

/**
 * Login component
 *
 * @author Iskakoff
 */
@Embeddable
public class Login {
    // username - unique field
    @Column(name = "username_column", unique = true, nullable = false, length = 32)
    private String username;
    // password - hashed password
    @Column(name = "password_column", nullable = false, length = 255)
    private String password;
    // sign that user was activated
    @Column(name = "active_column")
    private boolean active;
    @Column(name = "super_column")
    private boolean superUser;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public boolean getSuperUser() {
        return superUser;
    }

    public void setSuperUser(boolean superUser) {
        this.superUser = superUser;
    }
}
