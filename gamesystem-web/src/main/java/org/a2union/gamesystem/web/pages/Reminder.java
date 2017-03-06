package org.a2union.gamesystem.web.pages;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.commons.lang.StringUtils;
import org.a2union.gamesystem.model.user.IUserService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

/**
 * @author Iskakoff
 */
public class Reminder {

    private String passId;
    @Component(id = "passReminderForm", type = "Form")
    private Form passReminderForm;
    @Component(id = "passconfField", type = "PasswordField")
    private Field passconfField;
    @Component(id = "passwordField", type = "PasswordField")
    private Field passwordField;
    @Inject
    private ComponentResources resources;
    @Inject
    private IUserService userService;
    @InjectPage("success")
    private Success success;

    private String password;
    private String passconf;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassconf() {
        return passconf;
    }

    public void setPassconf(String passconf) {
        this.passconf = passconf;
    }


    public void onActivate(String passId) {
        this.passId = passId;
    }

    public void onActivate() {
        if(passId != null)
            onActivate(passId);
        else
            throw new IllegalStateException("game id must be specified");
    }

    public String onPassivate() {
        return passId;
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "passReminderForm")
    @Transactional(propagation = Propagation.REQUIRED)
    public Object success() {
        userService.restorePassword(passId, password);
        success.setMsgCode("pass-restored");
        return success;
    }

    @OnEvent(value = EventConstants.VALIDATE_FORM, component = "passReminderForm")
    @Transactional(propagation = Propagation.REQUIRED)
    public void validate() {
        if(userService.geyPasswordReminder(passId) == null)
            passReminderForm.recordError(resources.getMessages().get("noreminder"));
        if (!StringUtils.equals(passconf, password)) {
            passReminderForm.recordError(passconfField, resources.getMessages().get("notconfirmed"));
        }
        if(password !=null && password.length() > 10) {
            passReminderForm.recordError(passconfField, resources.getMessages().get("longpass"));
        }
    }
}
