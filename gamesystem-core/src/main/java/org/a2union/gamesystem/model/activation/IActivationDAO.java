/*
 * $Id: IActivationDAO.java 35 2009-03-10 14:49:19Z iskakoff $
 */
package org.a2union.gamesystem.model.activation;

import org.a2union.gamesystem.model.base.IBaseDAO;

import java.util.List;

/**
 * @author Iskakoff
 */
public interface IActivationDAO extends IBaseDAO<Activation> {
    Activation getActivationByUser(String userId);

    Activation getActivationByUserAndCode(String userId, String activationCode);

    List<Activation> getNonActiveActivations(final int pageSize);
}
