/*
 * $Id: Register.java 209 2010-10-16 06:31:26Z iskakoff $
 */
package org.a2union.gamesystem.web.pages;

import org.a2union.gamesystem.model.activation.IActivationService;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.model.user.Login;
import org.a2union.gamesystem.model.user.User;
import org.a2union.gamesystem.security.captcha.CaptchaIntegrationService;
import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Field;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PersistentLocale;
import org.apache.tapestry5.services.RequestGlobals;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Iskakoff
 */
public class Register {

    @Inject
    private PersistentLocale localeService;
    @Component(id = "register", type = "Form")
    private Form register;
    @Component(id = "verficationField", type = "TextField")
    private Field verficationField;
    @Component(id = "passconfField", type = "PasswordField")
    private Field passconfField;
    @Component(id = "passwordField", type = "PasswordField")
    private Field passwordField;
    @Inject
    private ComponentResources resources;
    @Inject
    private RequestGlobals requestGlobals;
    @InjectPage("success")
    private Success success;

    @Service("captchaIntegrationService")
    @Inject
    private CaptchaIntegrationService captchaService;

    @Inject
    @Service("userService")
    private IUserService userService;
    @Inject
    @Service("activationService")
    private IActivationService activationService;
    @Inject
    @Service("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    private String verification;
    private Login login = new Login();
    private String email;
    private String passconf;

    // url of captcha image
    public Link getImageURL() {
        return resources.createEventLink("image");
    }

    //executes when image is rendered to display CAPTCHA image
    public Object onImage() {
        return captchaService.generateImageChallenge(requestGlobals.getHTTPServletRequest().getSession().getId());
    }

    /**
     * register user and redirect to login page
     *
     * TODO add error handlers for service save method invocation to avoid ConstraintViolationException
     *
     * @return Login page class
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public Object onSuccess() {
        User user = new User();
        login.setPassword(passwordEncoder.encodePassword(passconf, null));
        user.setLogin(login);
        user.setEmail(email);
        userService.save(user);
        success.setMsgCode("registered");
        return success;
    }

    /**
     * check for correct input values
     *
     */
    public void onValidateForm() {
        String sessionId = requestGlobals.getHTTPServletRequest().getSession().getId();
        if (!captchaService.isValidUserResponse(StringUtils.upperCase(getVerification()), sessionId)) {
            register.recordError(verficationField, resources.getMessages().get("notverified"));
        }
        if (!StringUtils.equals(passconf, login.getPassword())) {
            register.recordError(passconfField, resources.getMessages().get("notconfirmed"));
        }
        if(login.getPassword() !=null && login.getPassword().length() > 10) {
            register.recordError(passconfField, resources.getMessages().get("longpass"));
        }
        if(userService.isUserExist(getEmail())) {
            register.recordError(passconfField, resources.getMessages().get("email-exists"));
        }
        try {
            if(userService.getByUsername(login.getUsername()) != null) {
                register.recordError(passconfField, resources.getMessages().get("user-exists"));
            }
        } catch (Exception e) {

        }
    }

    public String getVerification() {
        return verification;
    }

    public void setVerification(String verification) {
        this.verification = verification;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassconf() {
        return passconf;
    }

    public void setPassconf(String passconf) {
        this.passconf = passconf;
    }
}