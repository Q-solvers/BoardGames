/*
 * $Id: IfLogin.java 129 2009-10-03 14:51:25Z iskakoff $
 */
package org.a2union.componentslib.components;

import org.a2union.componentslib.services.IAuthenticationService;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Component that check is user logged in (also check if user is anonymous)
 *
 * @author Iskakoff
 */
public class IfLogin {
    @Inject
    private IAuthenticationService authenticationService;

    private boolean loggedIn() {
        return authenticationService.isAuthenticated();
    }

    /**
     * Optional parameter to invert the test. If true, then the body is rendered when the test parameter is false (not
     * true).
     *
     * @see org.apache.tapestry5.corelib.components.Unless
     */
    @Parameter
    private boolean negate;

    /**
     * An alternate {@link org.apache.tapestry5.Block} to render if the test parameter is false. The default, null,
     * means render nothing in that situation.
     */
    @Parameter(name = "else")
    private Block elseBlock;

    /**
     * Returns null if the test parameter is true, which allows normal rendering (of the body). If the test parameter is
     * false, returns the else parameter (this may also be null).
     *
     * @return block to render or null if defaul block to render
     */
    Object beginRender() {
        return loggedIn() != negate ? null : elseBlock;
    }

    /**
     * If the test parameter is true, then the body is rendered, otherwise not. The component does not have a template
     * or do any other rendering besides its body.
     *
     * @return true if render default block
     */
    boolean beforeRenderBody() {
        return loggedIn() != negate;
    }

    void setup(boolean negate, Block elseBlock) {
        this.negate = negate;
        this.elseBlock = elseBlock;
    }
}