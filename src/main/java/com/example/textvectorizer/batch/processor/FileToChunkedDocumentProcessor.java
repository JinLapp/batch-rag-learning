package com.example.textvectorizer.batch.processor;

import com.example.textvectorizer.chunking.TextChunkingService;
import com.example.textvectorizer.domain.ChunkedDocument;
import com.example.textvectorizer.domain.SourceFileDescriptor;
import com.example.textvectorizer.domain.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class FileToChunkedDocumentProcessor implements ItemProcessor<SourceFileDescriptor, ChunkedDocument> {

    private static final Logger log = LoggerFactory.getLogger(FileToChunkedDocumentProcessor.class);

    private final TextChunkingService textChunkingService;

    public FileToChunkedDocumentProcessor(TextChunkingService textChunkingService) {
        this.textChunkingService = textChunkingService;
    }

    @Override
    public ChunkedDocument process(SourceFileDescriptor item) throws Exception {
        log.info("Processing file into chunked document: {}", item.getSourcePath());

        String content = Files.readString(Path.of(item.getSourcePath()), StandardCharsets.UTF_8);
        List<TextChunk> chunks = textChunkingService.chunkText(content);

        log.info("File processed into {} chunk(s): {}", chunks.size(), item.getFileName());

        return new ChunkedDocument(
                item.getFileName(),
                item.getSourcePath(),
                item.getChecksum(),
                item.getDiscoveredAt(),
                chunks
        );
    }
}