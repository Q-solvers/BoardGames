/*
 * $Id: User.java 181 2010-02-16 19:38:28Z iskakoff $
 */
package org.a2union.gamesystem.model.user;

import org.a2union.gamesystem.model.activation.Activation;
import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.rate.Rate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_user")
public class User extends Base {
    // login component
    @Embedded
    private Login login;
    @Column(name = "email_column", length = 255)
    private String email;

    /**
     * Default zone for person. Used when person login to game
     */
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "DEFAULT_ZONE_COLUMN")
    @ForeignKey(name = "DEFAULT_ZONE_FK")
    private GameZone defaultZone;

    @OneToOne(targetEntity = Activation.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "ACTIVATION_ID")
    @ForeignKey(name = "ACTIVATION_PERSON_FK")
    @Index(name = "IDX_ACTIVATION_PERSON")
    @Cascade(CascadeType.ALL)
    private Activation activation;

    @OneToMany
    @JoinColumn(name = "USER_RATE_COLUMN")
    @Cascade(CascadeType.ALL)
    private Set<Rate> userRate = new HashSet<Rate>();

    /**
     * use bit-map for user notification preferences
     * 1 - movement notification
     * 2 - invitation notification
     * 4 - invitation accepted notification
     * 8 - invitation declined notification
     */
    @Column(name = "notification_column")
    private int notificationMap;

    public Set<Rate> getUserRate() {
        return Collections.unmodifiableSet(userRate);
    }

    public void addUserRate(Rate rate) {
        userRate.add(rate);
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Activation getActivation() {
        return activation;
    }

    public void setActivation(Activation activation) {
        this.activation = activation;
    }

    public GameZone getDefaultZone() {
        return defaultZone;
    }

    public void setDefaultZone(GameZone defaultZone) {
        this.defaultZone = defaultZone;
    }

    public int getNotificationMap() {
        return notificationMap;
    }

    public void setNotificationMap(int notificationMap) {
        this.notificationMap = notificationMap;
    }
}
