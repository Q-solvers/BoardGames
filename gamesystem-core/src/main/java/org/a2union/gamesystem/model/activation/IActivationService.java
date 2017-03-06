/*
 * $Id: IActivationService.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.model.activation;

import org.a2union.gamesystem.model.base.IBaseService;
import org.a2union.gamesystem.model.user.exceptions.ActivationException;

/**
 * @author Iskakoff
 */
public interface IActivationService extends IBaseService<Activation> {
    /**
     * activate not activated user
     * 
     * @param userId identifier of user to activate
     * @param activationCode code of activatition
     * @throws ActivationException if for some reason user can`t be activated
     */
    void activateUser(String userId, String activationCode) throws ActivationException;

    /**
     * send e-mail notification to user
     * 
     */
    void sendActivation();
}
