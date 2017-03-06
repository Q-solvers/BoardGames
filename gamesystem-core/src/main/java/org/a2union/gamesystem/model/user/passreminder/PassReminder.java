package org.a2union.gamesystem.model.user.passreminder;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_passreminder")
public class PassReminder extends Base {

    @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    @ForeignKey(name = "REMINDED_USER_FK")
    @Index(name = "IDX_REMINDED_USER")
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
