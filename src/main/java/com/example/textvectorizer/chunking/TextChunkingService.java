package com.example.textvectorizer.chunking;

import com.example.textvectorizer.chunking.model.ChunkingResult;
import com.example.textvectorizer.chunking.strategy.TextChunkingStrategy;
import com.example.textvectorizer.domain.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TextChunkingService {

    private static final Logger log = LoggerFactory.getLogger(TextChunkingService.class);

    private final List<TextChunkingStrategy> strategies;

    public TextChunkingService(List<TextChunkingStrategy> strategies) {
        this.strategies = strategies;
    }

    public List<TextChunk> chunkText(String text) {
        if (text == null || text.isBlank()) {
            log.warn("Received blank text for chunking.");
            return List.of();
        }

        for (TextChunkingStrategy strategy : strategies) {
            ChunkingResult result = strategy.tryChunk(text);

            if (result.isMatched()) {
                log.info("Chunking strategy selected: {}, produced {} chunk(s).",
                        result.getStrategyName(),
                        result.getChunks().size());
                return result.getChunks();
            }

            log.debug("Chunking strategy {} did not match.", strategy.getStrategyName());
        }

        log.warn("No chunking strategy matched. Returning empty chunk list.");
        return List.of();
    }
}