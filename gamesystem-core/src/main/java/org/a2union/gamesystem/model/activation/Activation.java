/*
 * $Id: Activation.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.model.activation;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity for save activation code to 
 *
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_activation")
public class Activation extends Base {

    @Column(name = "ACTIVATION_CODE_COLUMN")
    private String activationCode;

    @OneToOne(targetEntity = User.class,fetch = FetchType.LAZY, mappedBy = "activation")
    private User person;

    @Column(name = "NOTIFIED_COLUMN")
    private boolean notified;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public User getPerson() {
        return person;
    }

    public void setPerson(User person) {
        this.person = person;
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }
}
