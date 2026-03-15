package com.example.textvectorizer.domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

public class SourceFileDescriptor implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String fileName;
    private final String sourcePath;
    private final String checksum;
    private final Instant discoveredAt;

    public SourceFileDescriptor(String fileName, String sourcePath, String checksum, Instant discoveredAt) {
        this.fileName = fileName;
        this.sourcePath = sourcePath;
        this.checksum = checksum;
        this.discoveredAt = discoveredAt;
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

    @Override
    public String toString() {
        return "SourceFileDescriptor{" +
                "fileName='" + fileName + '\'' +
                ", sourcePath='" + sourcePath + '\'' +
                ", checksum='" + checksum + '\'' +
                ", discoveredAt=" + discoveredAt +
                '}';
    }
}