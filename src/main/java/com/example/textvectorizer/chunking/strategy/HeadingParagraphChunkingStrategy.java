package com.example.textvectorizer.chunking.strategy;

import com.example.textvectorizer.chunking.model.ChunkingResult;
import com.example.textvectorizer.domain.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Component
@Order(1)
public class HeadingParagraphChunkingStrategy implements TextChunkingStrategy {

    private static final Logger log = LoggerFactory.getLogger(HeadingParagraphChunkingStrategy.class);

    @Override
    public ChunkingResult tryChunk(String text) {
        if (text == null || text.isBlank()) {
            return ChunkingResult.noMatch(getStrategyName());
        }

        List<String> lines = normalizeLines(text);
        int headingCount = countHeadingLikeLines(lines);

        if (headingCount == 0) {
            log.debug("Heading strategy did not match: no heading-like lines found.");
            return ChunkingResult.noMatch(getStrategyName());
        }

        List<TextChunk> chunks = buildChunksFromHeadings(lines);

        if (chunks.isEmpty()) {
            log.debug("Heading strategy did not match: no chunks produced.");
            return ChunkingResult.noMatch(getStrategyName());
        }

        return ChunkingResult.matched(getStrategyName(), chunks);
    }

    @Override
    public String getStrategyName() {
        return "heading-paragraph";
    }

    private List<String> normalizeLines(String text) {
        String normalized = text.replace("\r\n", "\n").trim();
        String[] split = normalized.split("\n");

        List<String> lines = new ArrayList<>();
        for (String line : split) {
            lines.add(line.strip());
        }
        return lines;
    }

    private int countHeadingLikeLines(List<String> lines) {
        int count = 0;
        for (String line : lines) {
            if (isHeadingLike(line)) {
                count++;
            }
        }
        return count;
    }

    private List<TextChunk> buildChunksFromHeadings(List<String> lines) {
        List<TextChunk> chunks = new ArrayList<>();

        String currentHeading = null;
        StringBuilder currentBody = new StringBuilder();
        int chunkIndex = 0;

        for (String line : lines) {
            if (line.isBlank()) {
                if (!currentBody.isEmpty()) {
                    currentBody.append("\n");
                }
                continue;
            }

            if (isHeadingLike(line)) {
                if (currentHeading != null || !currentBody.isEmpty()) {
                    String chunkText = buildChunkText(currentHeading, currentBody.toString());
                    if (!chunkText.isBlank()) {
                        chunks.add(new TextChunk(chunkIndex++, chunkText));
                    }
                }

                currentHeading = line;
                currentBody = new StringBuilder();
            } else {
                if (!currentBody.isEmpty()) {
                    currentBody.append("\n");
                }
                currentBody.append(line);
            }
        }

        String lastChunk = buildChunkText(currentHeading, currentBody.toString());
        if (!lastChunk.isBlank()) {
            chunks.add(new TextChunk(chunkIndex, lastChunk));
        }

        return chunks;
    }

    private String buildChunkText(String heading, String body) {
        String cleanHeading = heading == null ? "" : heading.trim();
        String cleanBody = body == null ? "" : body.trim();

        if (cleanHeading.isBlank() && cleanBody.isBlank()) {
            return "";
        }

        if (cleanHeading.isBlank()) {
            return cleanBody;
        }

        if (cleanBody.isBlank()) {
            return cleanHeading;
        }

        return cleanHeading + "\n\n" + cleanBody;
    }

    private boolean isHeadingLike(String line) {
        if (line == null || line.isBlank()) {
            return false;
        }

        String trimmed = line.trim();

        if (trimmed.length() > 80) {
            return false;
        }

        if (trimmed.endsWith(":")) {
            return true;
        }

        if (trimmed.matches("^(#{1,6})\\s+.+")) {
            return true;
        }

        if (trimmed.matches("^\\d+(\\.\\d+)*\\s+.+")) {
            return true;
        }

        boolean hasLetters = trimmed.chars().anyMatch(Character::isLetter);
        boolean mostlyUppercase = hasLetters && trimmed.equals(trimmed.toUpperCase()) && trimmed.length() <= 60;

        if (mostlyUppercase) {
            return true;
        }

        boolean titleLike = trimmed.split("\\s+").length <= 8 && !trimmed.endsWith(".") && Character.isUpperCase(trimmed.charAt(0));
        return titleLike;
    }
}