package com.example.textvectorizer.batch.config;

import com.example.textvectorizer.batch.listener.BatchJobExecutionListener;
import com.example.textvectorizer.batch.listener.BatchStepExecutionListener;
import com.example.textvectorizer.batch.listener.ChunkPreparationStepListener;
import com.example.textvectorizer.batch.processor.FileToChunkedDocumentProcessor;
import com.example.textvectorizer.batch.reader.DiscoveredFileDescriptorReader;
import com.example.textvectorizer.batch.tasklet.DiscoverFilesTasklet;
import com.example.textvectorizer.batch.tasklet.InspectDiscoveredFilesTasklet;
import com.example.textvectorizer.batch.writer.LoggingChunkedDocumentWriter;
import com.example.textvectorizer.domain.ChunkedDocument;
import com.example.textvectorizer.domain.SourceFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchJobConfig {

    private static final Logger log = LoggerFactory.getLogger(BatchJobConfig.class);

    @Bean
    public Job textVectorizationJob(JobRepository jobRepository,
                                    Step discoverFilesStep,
                                    Step inspectDiscoveredFilesStep,
                                    Step chunkDocumentsStep,
                                    BatchJobExecutionListener jobExecutionListener) {
        log.info("Creating job bean: textVectorizationJob");

        return new JobBuilder("textVectorizationJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(discoverFilesStep)
                .next(inspectDiscoveredFilesStep)
                .next(chunkDocumentsStep)
                .build();
    }

    @Bean
    public Step discoverFilesStep(JobRepository jobRepository,
                                  PlatformTransactionManager transactionManager,
                                  DiscoverFilesTasklet discoverFilesTasklet,
                                  BatchStepExecutionListener stepExecutionListener) {
        log.info("Creating step bean: discoverFilesStep");

        return new StepBuilder("discoverFilesStep", jobRepository)
                .listener(stepExecutionListener)
                .tasklet(discoverFilesTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step inspectDiscoveredFilesStep(JobRepository jobRepository,
                                           PlatformTransactionManager transactionManager,
                                           InspectDiscoveredFilesTasklet inspectDiscoveredFilesTasklet,
                                           BatchStepExecutionListener stepExecutionListener) {
        log.info("Creating step bean: inspectDiscoveredFilesStep");

        return new StepBuilder("inspectDiscoveredFilesStep", jobRepository)
                .listener(stepExecutionListener)
                .tasklet(inspectDiscoveredFilesTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step chunkDocumentsStep(JobRepository jobRepository,
                                   PlatformTransactionManager transactionManager,
                                   DiscoveredFileDescriptorReader reader,
                                   FileToChunkedDocumentProcessor processor,
                                   LoggingChunkedDocumentWriter writer,
                                   BatchStepExecutionListener stepExecutionListener,
                                   ChunkPreparationStepListener chunkPreparationStepListener) {
        log.info("Creating step bean: chunkDocumentsStep");

        return new StepBuilder("chunkDocumentsStep", jobRepository)
                .<SourceFileDescriptor, ChunkedDocument>chunk(2, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .listener(stepExecutionListener)
                .listener(chunkPreparationStepListener)
                .build();
    }
}