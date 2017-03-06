package org.a2union.gamesystem.commons;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.quartz.JobDetailBean;

/**
 * @author Iskakoff
 */
public class MovementJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetailBean jobDetail = (JobDetailBean) jobExecutionContext.getJobDetail();
        JavaMailSender mailSender = (JavaMailSender) jobDetail.getJobDataMap().get("mailer");
        SimpleMailMessage message = (SimpleMailMessage) jobDetail.getJobDataMap().get("message");
        mailSender.send(message);
    }
}
