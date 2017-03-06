/*
 * $Id: ActivationJobDetailBean.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.commons;

import org.a2union.gamesystem.model.activation.IActivationService;
import org.springframework.scheduling.quartz.JobDetailBean;

/**
 * @author Iskakoff
 */
public class ActivationJobDetailBean extends JobDetailBean {
    IActivationService activationService;


    public IActivationService getActivationService() {
        return activationService;
    }

    public void setActivationService(IActivationService activationService) {
        this.activationService = activationService;
    }
}
