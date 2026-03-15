package com.example.textvectorizer.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

public class ChunkedDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final String sourcePath;
    private final String checksum;
    private final Instant discoveredAt;
    private final List<TextChunk> chunks;

    public ChunkedDocument(String fileName,
                           String sourcePath,
                           String checksum,
                           Instant discoveredAt,
                           List<TextChunk> chunks) {
        this.fileName = fileName;
        this.sourcePath = sourcePath;
        this.checksum = checksum;
        this.discoveredAt = discoveredAt;
        this.chunks = chunks;
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

    public List<TextChunk> getChunks() {
        return chunks;
    }

    @Override
    public String toString() {
        return "ChunkedDocument{" +
                "fileName='" + fileName + '\'' +
                ", sourcePath='" + sourcePath + '\'' +
                ", checksum='" + checksum + '\'' +
                ", discoveredAt=" + discoveredAt +
                ", chunks=" + chunks +
                '}';
    }
}