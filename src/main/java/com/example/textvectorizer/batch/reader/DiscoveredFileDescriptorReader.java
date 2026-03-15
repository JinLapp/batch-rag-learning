package com.example.textvectorizer.batch.reader;

import com.example.textvectorizer.batch.tasklet.DiscoverFilesTasklet;
import com.example.textvectorizer.domain.SourceFileDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
public class DiscoveredFileDescriptorReader implements ItemReader<SourceFileDescriptor> {

    private static final Logger log = LoggerFactory.getLogger(DiscoveredFileDescriptorReader.class);

    private List<SourceFileDescriptor> descriptors;
    private int currentIndex = 0;
    private boolean initialized = false;

    @Override
    @SuppressWarnings("unchecked")
    public SourceFileDescriptor read() {
        if (!initialized) {
            throw new IllegalStateException("Reader not initialized. Call init(...) before reading.");
        }

        if (descriptors == null || currentIndex >= descriptors.size()) {
            return null;
        }

        SourceFileDescriptor descriptor = descriptors.get(currentIndex++);
        log.info("Reader returning descriptor for file: {}", descriptor.getFileName());
        return descriptor;
    }

    public void init(ExecutionContext jobExecutionContext) {
        Object raw = jobExecutionContext.get(DiscoverFilesTasklet.EXECUTION_CONTEXT_KEY);

        if (raw == null) {
            this.descriptors = List.of();
            this.initialized = true;
            log.info("No file descriptors found in execution context. Reader initialized empty.");
            return;
        }

        this.descriptors = new ArrayList<>((List<SourceFileDescriptor>) raw);
        this.initialized = true;

        log.info("Reader initialized with {} discovered file descriptor(s).", descriptors.size());
    }
}