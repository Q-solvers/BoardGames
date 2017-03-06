/*
 * $Id: ViewDetail.java 182 2010-02-19 21:10:27Z iskakoff $
 */
package org.a2union.gamesystem.web.pages;

import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.Login;
import org.a2union.gamesystem.model.user.User;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Class for page to display current user personal information
 *
 * @author Iskakoff
 */
public class ViewDetail {

    @Inject
    @Service("userService")
    private IUserService userService;
    @Inject
    @Service("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    @Component(id = "userEditForm", type = "Form")
    private Form userEditForm;
    @Component(id = "passconfField", type = "PasswordField")
    private Field passconfField;
    @Component(id = "passwordField", type = "PasswordField")
    private Field passwordField;

    @Inject
    private ComponentResources resources;

    private boolean movement;
    private boolean invitation;
    private String passconf;
    private String password;

    private User person;

    @SetupRender
    /**
     * prepare for render page. get user from security context
     */
    public void prepareForRender() {
        person = userService.getCurrentUser();
        movement = (person.getNotificationMap()&1)!=0;
        invitation = (person.getNotificationMap()&2)!=0;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Object onSuccess() {
        person = userService.getCurrentUser();
        userService.updateUserDetails(person, passconf, movement, invitation);
        return this;
    }

    /**
     * check for correct input values
     *
     */
    public void onValidateForm() {
        if (!StringUtils.equals(passconf, password)) {
            userEditForm.recordError(passconfField, resources.getMessages().get("notconfirmed"));
        }
        if(password !=null && password.length() > 10) {
            userEditForm.recordError(passconfField, resources.getMessages().get("longpass"));
        }
    }

    public User getPerson() {
        return person;
    }

    public void setPerson(User person) {
        this.person = person;
    }

    public boolean isMovement() {
        return movement;
    }

    public void setMovement(boolean movement) {
        this.movement = movement;
    }

    public boolean isInvitation() {
        return invitation;
    }

    public void setInvitation(boolean invitation) {
        this.invitation = invitation;
    }

    public String getPassconf() {
        return passconf;
    }

    public void setPassconf(String passconf) {
        this.passconf = passconf;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}