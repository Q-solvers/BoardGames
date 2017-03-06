/*
 * $Id: ActivationJob.java 90 2009-06-30 17:54:28Z iskakoff $
 */
package org.a2union.gamesystem.commons;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @author Iskakoff
 */
public class ActivationJob implements Job {

    public void execute(JobExecutionContext context) throws JobExecutionException {
        ActivationJobDetailBean jobDetail = (ActivationJobDetailBean) context.getJobDetail();
        jobDetail.getActivationService().sendActivation();
    }
}
