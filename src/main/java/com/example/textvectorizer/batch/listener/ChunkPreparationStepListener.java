package com.example.textvectorizer.batch.listener;

import com.example.textvectorizer.batch.reader.DiscoveredFileDescriptorReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.ExitStatus;
import org.springframework.stereotype.Component;

@Component
public class ChunkPreparationStepListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(ChunkPreparationStepListener.class);

    private final DiscoveredFileDescriptorReader reader;

    public ChunkPreparationStepListener(DiscoveredFileDescriptorReader reader) {
        this.reader = reader;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("Initializing reader from job execution context for chunk preparation step.");
        reader.init(stepExecution.getJobExecution().getExecutionContext());
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return stepExecution.getExitStatus();
    }
}