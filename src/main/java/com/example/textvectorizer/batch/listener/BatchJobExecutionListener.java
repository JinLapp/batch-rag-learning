package com.example.textvectorizer.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class BatchJobExecutionListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(BatchJobExecutionListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        MDC.put("jobId", String.valueOf(jobExecution.getId()));
        log.info("Job started: jobName={}, jobId={}, parameters={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId(),
                jobExecution.getJobParameters());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        log.info("Job finished: jobName={}, jobId={}, status={}, exitStatus={}",
                jobExecution.getJobInstance().getJobName(),
                jobExecution.getId(),
                jobExecution.getStatus(),
                jobExecution.getExitStatus());

        if (jobExecution.getStatus() == BatchStatus.FAILED) {
            jobExecution.getAllFailureExceptions()
                    .forEach(ex -> log.error("Job failure reason", ex));
        }

        MDC.remove("jobId");
    }
}