package com.example.textvectorizer.chunking.strategy;

import com.example.textvectorizer.chunking.model.ChunkingResult;

public interface TextChunkingStrategy {

    ChunkingResult tryChunk(String text);

    String getStrategyName();
}