/*
 * : $
 */
package org.a2union.gamesystem.commontests;

import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.Login;
import org.a2union.gamesystem.model.user.User;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author Iskakoff
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@TransactionConfiguration(defaultRollback = false)
public class UserDependedTestCaseBase {

    @Autowired
    private IUserService userService;
    private static final String TESTUSERNAME = "test_user";
    private String userId;

    public void setUserService(IUserService userService) {
        this.userService = userService;
    }

    public IUserService getUserService() {
        return userService;
    }

    /**
     * create user for further tests
     */
    @Before
    public void setup() {
        User user = new User();
        Login login = new Login();
        user.setLogin(login);
        user.setEmail("test@email");
        login.setActive(false);
        login.setUsername(TESTUSERNAME);
        login.setPassword("pass");
        setUserId(userService.save(user));
    }

    @After
    public void cleanup() {
        User user = userService.getByUsername(TESTUSERNAME);
        userService.delete(user);
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
