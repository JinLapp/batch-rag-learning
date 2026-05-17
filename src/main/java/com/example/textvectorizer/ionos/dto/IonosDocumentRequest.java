package com.example.textvectorizer.ionos.dto;

import java.util.List;
import java.util.Map;

public class IonosDocumentRequest {

    private String text;
    private List<Double> embedding;
    private Map<String, Object> metadata;

    public IonosDocumentRequest() {
    }

    public IonosDocumentRequest(String text, List<Double> embedding, Map<String, Object> metadata) {
        this.text = text;
        this.embedding = embedding;
        this.metadata = metadata;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Double> getEmbedding() {
        return embedding;
    }

    public void setEmbedding(List<Double> embedding) {
        this.embedding = embedding;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }
}