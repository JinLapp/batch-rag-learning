package com.example.textvectorizer.domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class EmbeddedDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final String sourcePath;
    private final String checksum;
    private final List<EmbeddedChunk> embeddedChunks;

    public EmbeddedDocument(String fileName,
                            String sourcePath,
                            String checksum,
                            List<EmbeddedChunk> embeddedChunks) {
        this.fileName = fileName;
        this.sourcePath = sourcePath;
        this.checksum = checksum;
        this.embeddedChunks = embeddedChunks;
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

    public List<EmbeddedChunk> getEmbeddedChunks() {
        return embeddedChunks;
    }
}