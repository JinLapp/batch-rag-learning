package com.example.textvectorizer.batch.processor;

import com.example.textvectorizer.chunking.TextChunkingService;
import com.example.textvectorizer.domain.EmbeddedChunk;
import com.example.textvectorizer.domain.EmbeddedDocument;
import com.example.textvectorizer.domain.SourceFileDescriptor;
import com.example.textvectorizer.domain.TextChunk;
import com.example.textvectorizer.embedding.EmbeddingGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileToEmbeddedDocumentProcessor implements ItemProcessor<SourceFileDescriptor, EmbeddedDocument> {

    private static final Logger log = LoggerFactory.getLogger(FileToEmbeddedDocumentProcessor.class);

    private final TextChunkingService textChunkingService;
    private final EmbeddingGateway embeddingGateway;

    public FileToEmbeddedDocumentProcessor(TextChunkingService textChunkingService,
                                           EmbeddingGateway embeddingGateway) {
        this.textChunkingService = textChunkingService;
        this.embeddingGateway = embeddingGateway;
    }

    @Override
    public EmbeddedDocument process(SourceFileDescriptor item) throws Exception {
        log.info("Processing file into embedded document: {}", item.getSourcePath());

        String content = Files.readString(Path.of(item.getSourcePath()), StandardCharsets.UTF_8);
        List<TextChunk> chunks = textChunkingService.chunkText(content);

        List<EmbeddedChunk> embeddedChunks = new ArrayList<>();

        for (TextChunk chunk : chunks) {
            List<Double> embedding = embeddingGateway.embed(chunk.getSnippetText());

            embeddedChunks.add(new EmbeddedChunk(
                    item.getFileName(),
                    item.getSourcePath(),
                    item.getChecksum(),
                    item.getDiscoveredAt(),
                    chunk.getChunkIndex(),
                    chunk.getSnippetText(),
                    embedding
            ));
        }

        log.info("Embedded document created: fileName={}, chunkCount={}",
                item.getFileName(),
                embeddedChunks.size());

        return new EmbeddedDocument(
                item.getFileName(),
                item.getSourcePath(),
                item.getChecksum(),
                embeddedChunks
        );
    }
}