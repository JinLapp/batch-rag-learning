package com.example.textvectorizer.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchStartupRunner {

    private static final Logger log = LoggerFactory.getLogger(BatchStartupRunner.class);

    @Bean
    public CommandLineRunner runJobOnStartup(JobLauncher jobLauncher, Job textVectorizationJob) {
        return args -> {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("run.id", System.currentTimeMillis())
                    .toJobParameters();

            log.info("Launching Spring Batch job from CommandLineRunner with parameters: {}", jobParameters);

            JobExecution execution = jobLauncher.run(textVectorizationJob, jobParameters);

            log.info("Job execution finished with status: {}", execution.getStatus());
        };
    }
}