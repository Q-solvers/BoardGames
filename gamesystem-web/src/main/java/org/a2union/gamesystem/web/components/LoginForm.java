/*
 * $Id: $
 */
package org.a2union.gamesystem.web.components;

import org.a2union.componentslib.components.Dialog;
import org.a2union.gamesystem.model.user.IUserService;
import org.a2union.gamesystem.security.authentication.AuthenticationConstants;
import org.a2union.gamesystem.web.pages.Success;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.Session;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Iskakoff
 */
@IncludeStylesheet(value ="context:css/loginform.css")
public class LoginForm {
    @Inject
    private RequestGlobals request;
    private String errorMsg;
    private String email;
    @Inject
    @Service("userService")
    private IUserService userService;
    @Inject
    private ComponentResources resources;

    private String passId;
    @Component(id = "forgotPass", type = "black/dialog")
    private Dialog forgotPass;

    @InjectPage(value = "success")
    private Success success;

    /**
     *
     * @return login chech url - use default Spring-security url
     */
    public String getLoginCheckUrl() {
        return request.getRequest().getContextPath()+"/j_spring_security_check";
    }

    public boolean isFailed() {
        String error = request.getRequest().getParameter(AuthenticationConstants.ERROR_PARAMETER.getValue());
        return error!=null && error.equals("1");
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @OnEvent(value = EventConstants.SUCCESS, component = "forgotPass")
    @Transactional(propagation = Propagation.REQUIRED)
    public Object success() {
        userService.remindPassword(getEmail());
        success.setMsgCode("email-pass");
        return success;
    }

    @OnEvent(value = EventConstants.VALIDATE_FORM, component = "forgotPass")
    @Transactional(propagation = Propagation.REQUIRED)
    public void validate() {
        if(!userService.isUserExist(getEmail())) {
            forgotPass.recordError(resources.getMessages().get("invalid-email"));
        }
    }

    /**
     * Save page where we are to redirect if failed
     */
    public void setupRender(){
        Request request1 = request.getRequest();
        Session session = request1.getSession(false);
        if(session!=null)
            session.setAttribute(AuthenticationConstants.LOGIN_PAGE.getValue(), request1.getPath());
    }
}
