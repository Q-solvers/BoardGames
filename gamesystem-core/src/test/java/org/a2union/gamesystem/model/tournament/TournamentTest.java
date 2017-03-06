package org.a2union.gamesystem.model.tournament;

import org.junit.runner.RunWith;
import org.junit.Test;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.transaction.annotation.Transactional;
import org.a2union.gamesystem.model.game.tournament.ITournamentService;
import org.a2union.gamesystem.model.game.tournament.Tournament;
import org.a2union.gamesystem.model.game.tournament.Participant;
import org.a2union.gamesystem.model.user.ITestingUserService;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.model.user.Login;
import org.a2union.gamesystem.model.user.IUserService;

import java.util.Date;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext.xml", "classpath:applicationContext-test.xml"})
@TransactionConfiguration(defaultRollback = true)
public class TournamentTest {
    @Autowired
    private ITournamentService tournamentService;
    @Autowired
    private IUserService userService;

    @Test
    @Transactional
    public void createTournament(){
        ITestingUserService USTesting = (ITestingUserService) userService;
        User user1 = new User();
        Login login1 = new Login();
        login1.setUsername("test1");
        login1.setPassword("1");
        user1.setLogin(login1);
        user1.setEmail("test@test");
        USTesting.save(user1);
        Date expDate = new Date();
        Tournament createdTournament = tournamentService.createTournament(user1, 5, expDate);
        Tournament findedTournament = tournamentService.getById(createdTournament.getUUID());
        Assert.isTrue(createdTournament.getUUID().equals(findedTournament.getUUID()));
    }

    @Test
    @Transactional
    public void participateInTournament(){
        ITestingUserService USTesting = (ITestingUserService) userService;
        User user1 = new User();
        Login login1 = new Login();
        login1.setUsername("test1");
        login1.setPassword("1");
        user1.setLogin(login1);
        user1.setEmail("test@test");
        USTesting.save(user1);
        Date expDate = new Date();
        Tournament createdTournament = tournamentService.createTournament(user1, 5, expDate);
        Participant participant = new Participant();
        participant.setTournament(createdTournament);
        participant.setUser(user1);
        createdTournament.addParticipant(participant);

        List<Tournament> openTournaments = tournamentService.getOpenTournaments(user1);

        Assert.isTrue(openTournaments.size() == 1);
    }


}
