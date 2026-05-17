package com.example.textvectorizer.chunking.strategy;

import com.example.textvectorizer.config.AppProperties;
import com.example.textvectorizer.chunking.model.ChunkingResult;
import com.example.textvectorizer.domain.TextChunk;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(3)
public class FallbackFixedSizeChunkingStrategy implements TextChunkingStrategy {

    private final AppProperties appProperties;

    public FallbackFixedSizeChunkingStrategy(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public ChunkingResult tryChunk(String text) {
        if (text == null || text.isBlank()) {
            return ChunkingResult.matched(getStrategyName(), List.of());
        }

        int chunkSize = appProperties.getFallbackChunkSize();
        int overlap = appProperties.getFallbackChunkOverlap();

        List<TextChunk> chunks = new ArrayList<>();

        int start = 0;
        int chunkIndex = 0;
        int step = Math.max(1, chunkSize - overlap);

        while (start < text.length()) {
            int end = Math.min(start + chunkSize, text.length());
            String chunkText = text.substring(start, end).trim();

            if (!chunkText.isBlank()) {
                chunks.add(new TextChunk(chunkIndex++, chunkText));
            }

            if (end == text.length()) {
                break;
            }

            start += step;
        }

        return ChunkingResult.matched(getStrategyName(), chunks);
    }

    @Override
    public String getStrategyName() {
        return "fixed-size-fallback";
    }
}