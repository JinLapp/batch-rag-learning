package com.example.textvectorizer.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "ionos")
public class IonosProperties {

    @NotBlank
    private String apiKey;

    @NotBlank
    private String openAiBaseUrl;

    @NotBlank
    private String nativeBaseUrl;

    @NotBlank
    private String embeddingModel;

    @NotBlank
    private String documentCollectionId;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getOpenAiBaseUrl() {
        return openAiBaseUrl;
    }

    public void setOpenAiBaseUrl(String openAiBaseUrl) {
        this.openAiBaseUrl = openAiBaseUrl;
    }

    public String getNativeBaseUrl() {
        return nativeBaseUrl;
    }

    public void setNativeBaseUrl(String nativeBaseUrl) {
        this.nativeBaseUrl = nativeBaseUrl;
    }

    public String getEmbeddingModel() {
        return embeddingModel;
    }

    public void setEmbeddingModel(String embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public String getDocumentCollectionId() {
        return documentCollectionId;
    }

    public void setDocumentCollectionId(String documentCollectionId) {
        this.documentCollectionId = documentCollectionId;
    }
}
