package org.a2union.gamesystem.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.ApplicationListener;
import org.springframework.security.concurrent.SessionInformation;
import org.springframework.security.concurrent.SessionRegistry;
import org.springframework.security.concurrent.SessionRegistryUtils;
import org.springframework.security.event.authentication.AuthenticationSuccessEvent;
import org.springframework.security.ui.session.HttpSessionDestroyedEvent;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Iskakoff
 */
public class LoginEventListener implements ApplicationEventPublisherAware, ApplicationListener {

    private Map<String, Object> map = Collections.synchronizedMap(new HashMap<String, Object>());
    private ApplicationEventPublisher applicationEventPublisher;
    private SessionRegistry sessionRegistry;


    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof AuthenticationSuccessEvent) {
            AuthenticationSuccessEvent successEvent = (AuthenticationSuccessEvent) event;
            String sessionId = SessionRegistryUtils.obtainSessionIdFromAuthentication(successEvent.getAuthentication());
            UserDetails principal = (UserDetails) successEvent.getAuthentication().getPrincipal();
            map.put(sessionId, principal);
            applicationEventPublisher.publishEvent(new LoginEvent(principal.getUsername()));
        } else if (event instanceof HttpSessionDestroyedEvent) {
            HttpSessionDestroyedEvent destroyedEvent = (HttpSessionDestroyedEvent) event;
            String sessionId = destroyedEvent.getSession().getId();
            UserDetails principal = (UserDetails) map.get(sessionId);
            SessionInformation[] sessions = sessionRegistry.getAllSessions(principal, true);
            if (sessions != null && sessions.length > 1)
                return;
            if (principal == null)
                return;
//            WebApplicationContextUtils.getWebApplicationContext(destroyedEvent.getSession().getServletContext())
            applicationEventPublisher.publishEvent(new LogoutEvent(principal.getUsername()));
        }
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void setSessionRegistry(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }
}
