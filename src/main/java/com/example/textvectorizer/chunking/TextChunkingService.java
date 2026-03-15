package com.example.textvectorizer.chunking;

import com.example.textvectorizer.config.AppProperties;
import com.example.textvectorizer.domain.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunkingService {

    private static final Logger log = LoggerFactory.getLogger(TextChunkingService.class);

    private final AppProperties appProperties;

    public TextChunkingService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    public List<TextChunk> chunkText(String text) {
        if (text == null || text.isBlank()) {
            log.warn("Received blank text for chunking.");
            return List.of();
        }

        List<String> paragraphs = splitIntoParagraphs(text);
        List<TextChunk> chunks = buildChunksFromParagraphs(paragraphs);

        log.info("Chunking completed. Produced {} chunk(s).", chunks.size());
        return chunks;
    }

    private List<String> splitIntoParagraphs(String text) {
        String normalized = text.replace("\r\n", "\n").trim();
        String[] rawParagraphs = normalized.split("\\n\\s*\\n");

        List<String> paragraphs = new ArrayList<>();
        for (String paragraph : rawParagraphs) {
            String cleaned = paragraph.trim();
            if (!cleaned.isBlank()) {
                paragraphs.add(cleaned);
            }
        }

        return paragraphs;
    }

    private List<TextChunk> buildChunksFromParagraphs(List<String> paragraphs) {
        List<TextChunk> chunks = new ArrayList<>();

        int targetSize = appProperties.getFallbackChunkSize();
        int overlapSize = appProperties.getFallbackChunkOverlap();

        StringBuilder currentChunk = new StringBuilder();
        int chunkIndex = 0;

        for (String paragraph : paragraphs) {
            if (currentChunk.isEmpty()) {
                currentChunk.append(paragraph);
                continue;
            }

            if (currentChunk.length() + 2 + paragraph.length() <= targetSize) {
                currentChunk.append("\n\n").append(paragraph);
            } else {
                chunks.add(new TextChunk(chunkIndex++, currentChunk.toString()));

                String overlap = extractOverlap(currentChunk.toString(), overlapSize);
                currentChunk = new StringBuilder();

                if (!overlap.isBlank()) {
                    currentChunk.append(overlap).append("\n\n");
                }
                currentChunk.append(paragraph);
            }
        }

        if (!currentChunk.isEmpty()) {
            chunks.add(new TextChunk(chunkIndex, currentChunk.toString()));
        }

        return chunks;
    }

    private String extractOverlap(String text, int overlapSize) {
        if (overlapSize <= 0 || text.isBlank()) {
            return "";
        }

        if (text.length() <= overlapSize) {
            return text;
        }

        return text.substring(text.length() - overlapSize).trim();
    }
}