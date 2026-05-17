package com.example.textvectorizer.chunking.strategy;

import com.example.textvectorizer.config.AppProperties;
import com.example.textvectorizer.chunking.model.ChunkingResult;
import com.example.textvectorizer.domain.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(2)
public class SentenceWindowChunkingStrategy implements TextChunkingStrategy {

    private static final Logger log = LoggerFactory.getLogger(SentenceWindowChunkingStrategy.class);

    private final AppProperties appProperties;

    public SentenceWindowChunkingStrategy(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    @Override
    public ChunkingResult tryChunk(String text) {
        if (text == null || text.isBlank()) {
            return ChunkingResult.noMatch(getStrategyName());
        }

        List<String> sentences = splitIntoSentences(text);

        if (sentences.size() < 3) {
            log.debug("Sentence strategy did not match: too few sentences found.");
            return ChunkingResult.noMatch(getStrategyName());
        }

        List<TextChunk> chunks = buildSentenceWindows(sentences);

        if (chunks.isEmpty()) {
            return ChunkingResult.noMatch(getStrategyName());
        }

        return ChunkingResult.matched(getStrategyName(), chunks);
    }

    @Override
    public String getStrategyName() {
        return "sentence-window";
    }

    private List<String> splitIntoSentences(String text) {
        String normalized = text.replace("\r\n", "\n").replaceAll("\\s+", " ").trim();
        String[] raw = normalized.split("(?<=[.!?])\\s+");

        List<String> sentences = new ArrayList<>();
        for (String sentence : raw) {
            String cleaned = sentence.trim();
            if (!cleaned.isBlank()) {
                sentences.add(cleaned);
            }
        }
        return sentences;
    }

    private List<TextChunk> buildSentenceWindows(List<String> sentences) {
        List<TextChunk> chunks = new ArrayList<>();

        int windowSize = appProperties.getChunkSentenceWindowSize();
        int overlap = appProperties.getChunkSentenceOverlap();

        if (windowSize <= 0) {
            return chunks;
        }

        int step = Math.max(1, windowSize - overlap);
        int chunkIndex = 0;

        for (int start = 0; start < sentences.size(); start += step) {
            int end = Math.min(start + windowSize, sentences.size());
            List<String> window = sentences.subList(start, end);

            if (window.isEmpty()) {
                continue;
            }

            String chunkText = String.join(" ", window).trim();
            if (!chunkText.isBlank()) {
                chunks.add(new TextChunk(chunkIndex++, chunkText));
            }

            if (end == sentences.size()) {
                break;
            }
        }

        return chunks;
    }
}