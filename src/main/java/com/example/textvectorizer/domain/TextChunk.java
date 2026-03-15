package com.example.textvectorizer.domain;

import java.io.Serial;
import java.io.Serializable;

public class TextChunk implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int chunkIndex;
    private final String snippetText;

    public TextChunk(int chunkIndex, String snippetText) {
        this.chunkIndex = chunkIndex;
        this.snippetText = snippetText;
    }

    public int getChunkIndex() {
        return chunkIndex;
    }

    public String getSnippetText() {
        return snippetText;
    }

    @Override
    public String toString() {
        return "TextChunk{" +
                "chunkIndex=" + chunkIndex +
                ", snippetText='" + snippetText + '\'' +
                '}';
    }
}