/*
 * $Id: Activation.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.web.pages;

import org.a2union.gamesystem.model.activation.IActivationService;
import org.a2union.gamesystem.model.user.exceptions.ActivationException;
import org.apache.log4j.Logger;
import org.apache.tapestry5.annotations.Service;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * @author Iskakoff
 */
public class Activation {

    private static final Logger log = Logger.getLogger(Activation.class.getName());

    @Inject
    @Service("activationService")
    private IActivationService activationService;

    private boolean success = false;

    public void onActivate(String userId, String activationCode) {
        try {
            activationService.activateUser(userId, activationCode);
            success = true;
        } catch (ActivationException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            log.error(e.getMessage(), e);
            success = false;
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
