package org.a2union.gamesystem.model.user.rate;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.game.zone.GameZoneType;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_rate")
public class Rate extends Base {

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_RATE_COLUMN")
    @ForeignKey(name = "USER_RATE_FK")
    private User user;
    @Column(name = "ZONE_TYPE_COLUMN")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.zone.GameZoneType")}
    )
    private GameZoneType zoneType;

    @Column(name = "RATE_VALUE_COLUMN")
    private Integer value;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public GameZoneType getZoneType() {
        return zoneType;
    }

    public void setZoneType(GameZoneType zoneType) {
        this.zoneType = zoneType;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
