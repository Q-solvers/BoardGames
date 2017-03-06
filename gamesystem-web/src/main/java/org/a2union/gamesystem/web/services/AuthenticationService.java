package org.a2union.gamesystem.web.services;

import org.a2union.componentslib.services.IAuthenticationService;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;

/**
 * @author Iskakoff
 */
public class AuthenticationService implements IAuthenticationService {
    @Override
    public boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication == null || authentication instanceof AnonymousAuthenticationToken || ArrayUtils.isEmpty(authentication.getAuthorities()));
    }

    @Override
    public boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null)
            return false;
        GrantedAuthority[] authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if("ROLE_ADMIN".equals(authority.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
