package org.a2union.gamesystem.model.game.invitation;

import org.a2union.gamesystem.model.game.IGameService;
import org.a2union.gamesystem.model.game.chess.IChessGameService;
import org.a2union.gamesystem.model.game.side.GameSideType;
import org.a2union.gamesystem.model.user.ITestingUserService;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.Login;
import org.a2union.gamesystem.model.user.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * @author Iskakoff
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true)
public class InvitationTest {

    @Autowired
    private IChessGameService gameService;

    @Autowired
    private IGameInvitationService gameInvitationService;

    @Autowired
    private IUserService userService;

//    @Autowired
//    private AuthenticationManager authenticationManager;

    @Test
    @Transactional
    public void invite() {
        ITestingUserService USTesting = (ITestingUserService) userService;
        User user1 = new User();
        User user2 = new User();
        Login login1 = new Login();
        Login login2 = new Login();
        login1.setUsername("test1");
        login1.setPassword("1");
        user1.setLogin(login1);
        user1.setEmail("test@test");
        USTesting.save(user1);
        login2.setUsername("test2");
        login2.setPassword("1");
        user2.setLogin(login2);
        user2.setEmail("test2@test");
        USTesting.save(user2);
        USTesting.setCurrentUserId(user1.getLogin().getUsername());
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("test1","1"));
        String id = gameInvitationService.inviteUser(user2, gameService.getMainZone(), GameSideType.FIRST, GameSideType.SECOND);

        GameInvitation gameInvitation = gameInvitationService.getById(id);
        //try to accept invitation
        int old1 = gameService.getActiveGames(gameService.getMainZone().getUUID(), user1).size();
        int old2 = gameService.getActiveGames(gameService.getMainZone().getUUID(), user2).size();
        USTesting.setCurrentUserId(user2.getLogin().getUsername());
        gameInvitationService.acceptInvitation(gameInvitation);

        Assert.isTrue(gameService.getActiveGames(gameService.getMainZone().getUUID(), user1).size()>old1);
        Assert.isTrue(gameService.getActiveGames(gameService.getMainZone().getUUID(), user2).size()>old2);
        Assert.isNull(gameInvitationService.getById(id));

    }

    @Test
    @Transactional
    public void declineInvitation() {
        ITestingUserService USTesting = (ITestingUserService) userService;
        User user1 = new User();
        User user2 = new User();
        Login login1 = new Login();
        Login login2 = new Login();
        login1.setUsername("test1");
        login1.setPassword("1");
        user1.setLogin(login1);
        user1.setEmail("test@test");
        USTesting.save(user1);
        login2.setUsername("test2");
        login2.setPassword("1");
        user2.setLogin(login2);
        user2.setEmail("test2@test");
        USTesting.save(user2);
        USTesting.setCurrentUserId(user1.getLogin().getUsername());
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("test1","1"));
        String id = gameInvitationService.inviteUser(user2, gameService.getMainZone(), GameSideType.FIRST, GameSideType.SECOND);

        GameInvitation gameInvitation = gameInvitationService.getById(id);
        //try to accept invitation
        USTesting.setCurrentUserId(user2.getLogin().getUsername());
        int old1 = gameService.getActiveGames(gameService.getMainZone().getUUID(), user1).size();
        int old2 = gameService.getActiveGames(gameService.getMainZone().getUUID(), user2).size();
        gameInvitationService.declineInvitation(gameInvitation);

        Assert.isTrue(gameService.getActiveGames(gameService.getMainZone().getUUID(), user1).size()==old1);
        Assert.isTrue(gameService.getActiveGames(gameService.getMainZone().getUUID(), user2).size()==old2);
        Assert.isNull(gameInvitationService.getById(id));

    }
}
