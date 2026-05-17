package com.example.textvectorizer.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StartupValidationConfig {

    private static final Logger log = LoggerFactory.getLogger(StartupValidationConfig.class);

    @Bean
    CommandLineRunner validateIonosConfig(IonosProperties ionosProperties) {
        return args -> log.info(
                "IONOS config loaded: dryRun={}, openAiBaseUrl={}, nativeBaseUrl={}, embeddingModel={}, documentCollectionId={}",
                ionosProperties.isDryRun(),
                ionosProperties.getOpenAiBaseUrl(),
                ionosProperties.getNativeBaseUrl(),
                ionosProperties.getEmbeddingModel(),
                ionosProperties.getDocumentCollectionId()
        );
    }
}