package com.example.textvectorizer.config;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "app")
public class AppProperties {

    @NotBlank
    private String inputDirectory;

    @Min(1)
    private int chunkSentenceWindowSize = 5;

    @Min(0)
    private int chunkSentenceOverlap = 1;

    @Min(100)
    private int fallbackChunkSize = 1200;

    @Min(0)
    private int fallbackChunkOverlap = 150;

    private boolean recursiveScan = false;

    public String getInputDirectory() {
        return inputDirectory;
    }

    public void setInputDirectory(String inputDirectory) {
        this.inputDirectory = inputDirectory;
    }

    public int getChunkSentenceWindowSize() {
        return chunkSentenceWindowSize;
    }

    public void setChunkSentenceWindowSize(int chunkSentenceWindowSize) {
        this.chunkSentenceWindowSize = chunkSentenceWindowSize;
    }

    public int getChunkSentenceOverlap() {
        return chunkSentenceOverlap;
    }

    public void setChunkSentenceOverlap(int chunkSentenceOverlap) {
        this.chunkSentenceOverlap = chunkSentenceOverlap;
    }

    public int getFallbackChunkSize() {
        return fallbackChunkSize;
    }

    public void setFallbackChunkSize(int fallbackChunkSize) {
        this.fallbackChunkSize = fallbackChunkSize;
    }

    public int getFallbackChunkOverlap() {
        return fallbackChunkOverlap;
    }

    public void setFallbackChunkOverlap(int fallbackChunkOverlap) {
        this.fallbackChunkOverlap = fallbackChunkOverlap;
    }

    public boolean isRecursiveScan() {
        return recursiveScan;
    }

    public void setRecursiveScan(boolean recursiveScan) {
        this.recursiveScan = recursiveScan;
    }
}
