package com.example.textvectorizer.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class BatchStepExecutionListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(BatchStepExecutionListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        MDC.put("stepName", stepExecution.getStepName());
        log.info("Step started: stepName={}, stepExecutionId={}",
                stepExecution.getStepName(),
                stepExecution.getId());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("Step finished: stepName={}, stepExecutionId={}, status={}, readCount={}, writeCount={}, skipCount={}",
                stepExecution.getStepName(),
                stepExecution.getId(),
                stepExecution.getStatus(),
                stepExecution.getReadCount(),
                stepExecution.getWriteCount(),
                stepExecution.getSkipCount());

        MDC.remove("stepName");
        return stepExecution.getExitStatus();
    }
}