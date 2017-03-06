/*
 * $Id: BlackModule.java 132 2009-10-03 14:53:51Z iskakoff $
 */
package org.a2union.componentslib.services;

import org.apache.tapestry5.services.LibraryMapping;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;

/**
 * @author Iskakoff
 */
public class BlackModule {
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("black", "org.a2union.componentslib"));
    }

    public static void contributeClasspathAssetAliasManager(MappedConfiguration<String, String> configuration)
    {
        configuration.add("componentslib/1.0", "org/a2union/componentslib");
    }
}
