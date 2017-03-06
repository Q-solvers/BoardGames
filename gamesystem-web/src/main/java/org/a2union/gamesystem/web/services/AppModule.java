/*
 * $Id: AppModule.java 200 2010-10-14 14:48:43Z iskakoff $
 */
package org.a2union.gamesystem.web.services;

import org.a2union.componentslib.services.IAuthenticationService;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;

/**
 * @author Iskakoff
 */
public class AppModule {

    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration) {
        // configure available locales. By default only en locale present
        configuration.add("tapestry.supported-locales", "ru_RU,en");
    }

    public static void contributeIgnoredPathsFilter(Configuration<String> configuration) {
        configuration.add("/.*swf");
        configuration.add("/messagebroker/.*");
        configuration.add("/messagebroker.*");
    }

    public static void bind(ServiceBinder binder) {
        binder.bind(SessionService.class, SessionServiceImpl.class);
        binder.bind(IAuthenticationService.class, AuthenticationService.class);
    }

}
