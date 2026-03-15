package com.example.textvectorizer.batch.tasklet;

import com.example.textvectorizer.config.AppProperties;
import com.example.textvectorizer.domain.SourceFileDescriptor;
import com.example.textvectorizer.metadata.service.ImportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Stream;

@Component
@StepScope
public class DiscoverFilesTasklet implements Tasklet {

    private static final Logger log = LoggerFactory.getLogger(DiscoverFilesTasklet.class);
    public static final String EXECUTION_CONTEXT_KEY = "discoveredFileDescriptors";

    private final AppProperties appProperties;
    private final ImportHistoryService importHistoryService;

    public DiscoverFilesTasklet(AppProperties appProperties,
                                ImportHistoryService importHistoryService) {
        this.appProperties = appProperties;
        this.importHistoryService = importHistoryService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        Path inputDir = Paths.get(appProperties.getInputDirectory());

        log.info("Starting file discovery in directory: {}", inputDir.toAbsolutePath());

        if (!Files.exists(inputDir)) {
            log.warn("Input directory does not exist. Creating it: {}", inputDir.toAbsolutePath());
            Files.createDirectories(inputDir);
        }

        if (!Files.isDirectory(inputDir)) {
            throw new IllegalStateException("Configured input path is not a directory: " + inputDir.toAbsolutePath());
        }

        List<SourceFileDescriptor> descriptors = discoverEligibleFiles(inputDir);

        ExecutionContext jobExecutionContext = chunkContext.getStepContext()
                .getStepExecution()
                .getJobExecution()
                .getExecutionContext();

        jobExecutionContext.put(EXECUTION_CONTEXT_KEY, new ArrayList<>(descriptors));

        contribution.incrementReadCount();

        log.info("File discovery completed. {} new file(s) prepared for downstream processing.",
                descriptors.size());

        return RepeatStatus.FINISHED;
    }

    private List<SourceFileDescriptor> discoverEligibleFiles(Path inputDir) throws IOException, NoSuchAlgorithmException {
        List<SourceFileDescriptor> descriptors = new ArrayList<>();

        try (Stream<Path> pathStream = appProperties.isRecursiveScan()
                ? Files.walk(inputDir)
                : Files.list(inputDir)) {

            pathStream
                    .filter(Files::isRegularFile)
                    .filter(this::isTextFile)
                    .sorted()
                    .forEach(path -> {
                        try {
                            String checksum = calculateChecksum(path);
                            String fileName = path.getFileName().toString();
                            String absolutePath = path.toAbsolutePath().toString();

                            if (importHistoryService.isAlreadyImported(checksum)) {
                                log.info("Skipping already imported file: {}", absolutePath);
                                return;
                            }

                            SourceFileDescriptor descriptor = new SourceFileDescriptor(
                                    fileName,
                                    absolutePath,
                                    checksum,
                                    Instant.now()
                            );

                            descriptors.add(descriptor);

                            log.info("Discovered new file: fileName={}, sourcePath={}, checksum={}",
                                    fileName, absolutePath, checksum);
                        } catch (Exception ex) {
                            throw new RuntimeException("Failed while discovering file: " + path, ex);
                        }
                    });
        }

        return descriptors;
    }

    private boolean isTextFile(Path path) {
        return path.getFileName().toString().toLowerCase().endsWith(".txt");
    }

    private String calculateChecksum(Path path) throws IOException, NoSuchAlgorithmException {
        String content = Files.readString(path, StandardCharsets.UTF_8);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hash);
    }
}