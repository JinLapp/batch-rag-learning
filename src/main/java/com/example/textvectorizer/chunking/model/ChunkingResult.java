package com.example.textvectorizer.chunking.model;

import com.example.textvectorizer.domain.TextChunk;

import java.util.List;

public class ChunkingResult {

    private final boolean matched;
    private final String strategyName;
    private final List<TextChunk> chunks;

    public ChunkingResult(boolean matched, String strategyName, List<TextChunk> chunks) {
        this.matched = matched;
        this.strategyName = strategyName;
        this.chunks = chunks;
    }

    public boolean isMatched() {
        return matched;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public List<TextChunk> getChunks() {
        return chunks;
    }

    public static ChunkingResult noMatch(String strategyName) {
        return new ChunkingResult(false, strategyName, List.of());
    }

    public static ChunkingResult matched(String strategyName, List<TextChunk> chunks) {
        return new ChunkingResult(true, strategyName, chunks);
    }
}