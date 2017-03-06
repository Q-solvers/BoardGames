package org.a2union.gamesystem.model.game.invitation;

import org.a2union.gamesystem.model.base.Base;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author Iskakoff
 */
@Entity
@Table(name = "tbl_game_invitation")
public class GameInvitation extends Base {
    @JoinColumn(name = "INVITED_USER", nullable = false)
    @ManyToOne
    private User invitedUser;
    @JoinColumn(name = "INVITING_USER", nullable = false)
    @ManyToOne
    private User invitingUser;
    @JoinColumn(name = "INVITED_GAME_ZONE", nullable = false)
    @ManyToOne
    private GameZone invitedGameZone;
    @Column(name = "INVITED_SIDE_TYPE")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.side.GameSideType")}
    )
    private GameSideType invitedUserSideType;
    @Column(name = "INVITING_SIDE_TYPE")
    @Type(
            type = "org.hibernate.type.EnumType",
            parameters = {@Parameter(name = "enumClass", value = "org.a2union.gamesystem.model.game.side.GameSideType")}
    )
    private GameSideType invitedingUserSideType;

    //TODO
    @Transient
    private String name;

    public User getInvitedUser() {
        return invitedUser;
    }

    public void setInvitedUser(User invitedUser) {
        this.invitedUser = invitedUser;
    }

    public User getInvitingUser() {
        return invitingUser;
    }

    public void setInvitingUser(User invitingUser) {
        this.invitingUser = invitingUser;
    }

    public GameZone getInvitedGameZone() {
        return invitedGameZone;
    }

    public void setInvitedGameZone(GameZone invitedGameZone) {
        this.invitedGameZone = invitedGameZone;
    }

    public GameSideType getInvitedUserSideType() {
        return invitedUserSideType;
    }

    public void setInvitedUserSideType(GameSideType invitedUserSideType) {
        this.invitedUserSideType = invitedUserSideType;
    }

    public GameSideType getInvitedingUserSideType() {
        return invitedingUserSideType;
    }

    public void setInvitedingUserSideType(GameSideType invitedingUserSideType) {
        this.invitedingUserSideType = invitedingUserSideType;
    }

    public String getName() {
        if(name == null) {
            name = invitingUser.getLogin().getUsername() +" vs " + invitedUser.getLogin().getUsername();
        }
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
