package org.a2union.gamesystem.model.game.invitation;

import org.a2union.gamesystem.model.base.BaseDAO;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.game.zone.GameZone;
import org.a2union.gamesystem.model.user.User;
import org.hibernate.Query;

import java.util.List;

/**
 * @author Iskakoff
 */
public class GameInvitationDAO extends BaseDAO<GameInvitation> implements IGameInvitationDAO {
    @Override
    public Class<GameInvitation> getBaseClass() {
        return GameInvitation.class;
    }

    @Override
    public String createInvitation(User invitedUser, User invitingUser, GameZone zone, GameSideType invitingSideType, GameSideType invitedSideType) {
        GameInvitation invitation = new GameInvitation();
        invitation.setInvitedUser(invitedUser);
        invitation.setInvitingUser(invitingUser);
        invitation.setInvitedGameZone(zone);
        invitation.setInvitedingUserSideType(invitingSideType);
        invitation.setInvitedUserSideType(invitedSideType);
        return save(invitation);
    }

    //TODO implement newest invitation obtaining
    @Override
    public int countInvitationsByUser(User currentUser, GameZone zone, boolean unread) {
        List result = getHibernateTemplate().find("select count(*) from " + GameInvitation.class.getName() + " i where i.invitedUser=? and i.invitedGameZone=? " + (unread ? "" : ""), new Object[]{currentUser, zone});
        return ((Number)result.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<GameInvitation> getInvitationsByUserPage(User currentUser, GameZone zone, int startIndex, int size) {
        Query q = getSession().createQuery("from " + GameInvitation.class.getName() +" i where i.invitedUser=:iuser and i.invitedGameZone=:izone");
        q.setParameter("iuser", currentUser);
        q.setParameter("izone", zone);
        q.setFirstResult(startIndex);
        q.setMaxResults(size);
        return q.list();
    }
}
