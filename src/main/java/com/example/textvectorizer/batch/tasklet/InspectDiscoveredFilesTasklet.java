package com.example.textvectorizer.batch.tasklet;

import com.example.textvectorizer.domain.SourceFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@StepScope
public class InspectDiscoveredFilesTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(InspectDiscoveredFilesTasklet.class);

    @Override
    @SuppressWarnings("unchecked")
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        List<SourceFileDescriptor> descriptors =
                (List<SourceFileDescriptor>) jobExecutionContext.get(DiscoverFilesTasklet.EXECUTION_CONTEXT_KEY);

        if (descriptors == null || descriptors.isEmpty()) {
            log.info("No discovered files found in execution context.");
            return RepeatStatus.FINISHED;
        }

        log.info("Inspecting {} discovered file descriptor(s) from execution context.", descriptors.size());

        for (SourceFileDescriptor descriptor : descriptors) {
            log.info("Descriptor: {}", descriptor);
        }

        contribution.incrementReadCount();
        return RepeatStatus.FINISHED;
    }
}