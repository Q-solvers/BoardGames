/*
 * $Id: ActivationTest.java 181 2010-02-16 19:38:28Z iskakoff $
 */
package org.a2union.gamesystem.model.activation;

import org.a2union.gamesystem.commontests.UserDependedTestCaseBase;
import org.a2union.gamesystem.model.user.exceptions.ActivationException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Iskakoff
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TransactionConfiguration(defaultRollback = false)
public class ActivationTest extends UserDependedTestCaseBase {

    @Autowired
    private IActivationService activationService;
    @Autowired
    private IActivationDAO activationDAO;

    /**
     * Check for user activation object creation when user has been created in @Before method
     */
    @Test
    public void testCreatedActivation() {
        Activation activationByUser = activationDAO.getActivationByUser(getUserId());
        Assert.assertNotNull(activationByUser);
    }

    /**
     * Check for user activation
     *
     * @throws ActivationException can`t activate user - test fails
     */
    @Test
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void testUserActivation() throws ActivationException {
        Activation activationByUser = activationDAO.getActivationByUser(getUserId());
        String ac = activationByUser.getActivationCode();
        activationService.activateUser(getUserId(), ac);
        Assert.assertTrue(activationByUser.getPerson().getLogin().isActive());
        Assert.assertTrue(activationByUser.getPerson().getUserRate().size()>0);
    }
}
