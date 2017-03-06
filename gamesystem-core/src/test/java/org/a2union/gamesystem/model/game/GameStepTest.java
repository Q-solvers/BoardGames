package org.a2union.gamesystem.model.game;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.util.List;

/**
 * @author Iskakoff
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = false)
public class GameStepTest {

    @Autowired
    private IGameDAO gameDAO;

    @Test
    public void testStepQuery() {
        List all = gameDAO.getAll();
        for (Object o : all) {
            gameDAO.getLastStepByGame((GameBase) o);
        }
    }
}
