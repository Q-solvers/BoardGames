/*
 * $Id: GameZone.java 133 2009-10-04 08:23:51Z iskakoff $
 */
package org.a2union.gamesystem.model.game.zone;

import org.a2union.gamesystem.model.base.Base;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * GameZone is needed to divide diferent game types for one user. One user
 * can paticipate in several game types such as chess, nards etc. Each game type
 * correspond one GameZone. Each GameZone can nave only one user.
 * new game type must have its GameZone subclass.
 *
 * @author a.petrov
 *         Date: 16.02.2009 12:01:16
 */
@Entity
@Table(name = "tbl_game_zone")
public class GameZone extends Base {

    @Column(name = "MAIN_COLUMN", nullable = false)
    private boolean main = false;

    @Column(name = "ZONE_TYPE", unique = true)
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.zone.GameZoneType")}
    )
    private GameZoneType type;

    public boolean isMain() {
        return main;
    }

    public void setMain(boolean main) {
        this.main = main;
    }

    public GameZoneType getType() {
        return type;
    }

    public void setType(GameZoneType type) {
        this.type = type;
    }
}
