/*
 * $Id: $
 */
package org.a2union.gamesystem.security.authentication;

import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.ui.TargetUrlResolver;
import org.springframework.security.ui.TargetUrlResolverImpl;
import org.springframework.security.ui.webapp.AuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 *
 *
 * @author Iskakoff
 */
public class CustomAuthenticationProcessingFilter extends AuthenticationProcessingFilter {

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        TargetUrlResolver resolver = getTargetUrlResolver();
        if (resolver instanceof TargetUrlResolverImpl) {
            TargetUrlResolverImpl targetUrlResolver = (TargetUrlResolverImpl) resolver;
            targetUrlResolver.setJustUseSavedRequestOnGet(true);
        }
    }

    /**
     * if there is session and it contains login page then redirect to it
     * use default failure url otherwise
     *
     * @param request http-request
     * @param failed authntication exception
     * @return redirection url
     */
    @Override
    protected String determineFailureUrl(HttpServletRequest request, AuthenticationException failed) {
        HttpSession session = request.getSession(false);
        if(session!= null && session.getAttribute(AuthenticationConstants.LOGIN_PAGE.getValue())!=null) {
            return session.getAttribute(AuthenticationConstants.LOGIN_PAGE.getValue()) + "?error=1";
        }
        return super.determineFailureUrl(request, failed);
    }

    /**
     * cleanup login failure url
     *
     * @param request http-request
     * @param response http-response
     * @param authResult authentication result
     * @throws IOException if something terrible happens
     */
    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) throws IOException {
        super.onSuccessfulAuthentication(request, response, authResult);
        cleanup(request);
    }

    /**
     * cleanup login failure url
     *
     * @param request http-request
     * @param response http-response
     * @param failed authentication exception
     * @throws IOException if something terrible happens
     */
    @Override
    protected void onUnsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        super.onUnsuccessfulAuthentication(request, response, failed);
        cleanup(request);
    }

    /**
     * cleanup session attributes
     * @param request http-request
     */
    private void cleanup(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session!=null)
            session.removeAttribute(AuthenticationConstants.LOGIN_PAGE.getValue());
    }

}
