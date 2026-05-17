package com.example.textvectorizer.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class EmbeddedChunk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final String sourcePath;
    private final String checksum;
    private final Instant discoveredAt;
    private final int chunkIndex;
    private final String snippetText;
    private final List<Double> embedding;

    public EmbeddedChunk(String fileName,
                         String sourcePath,
                         String checksum,
                         Instant discoveredAt,
                         int chunkIndex,
                         String snippetText,
                         List<Double> embedding) {
        this.fileName = fileName;
        this.sourcePath = sourcePath;
        this.checksum = checksum;
        this.discoveredAt = discoveredAt;
        this.chunkIndex = chunkIndex;
        this.snippetText = snippetText;
        this.embedding = embedding;
    }

    public String getFileName() {
        return fileName;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getChecksum() {
        return checksum;
    }

    public Instant getDiscoveredAt() {
        return discoveredAt;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public String getSnippetText() {
        return snippetText;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }
}