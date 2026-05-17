package com.example.textvectorizer.embedding;

import com.example.textvectorizer.config.IonosProperties;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class IonosEmbeddingGateway implements EmbeddingGateway {

    private static final Logger log = LoggerFactory.getLogger(IonosEmbeddingGateway.class);

    private final WebClient webClient;
    private final IonosProperties ionosProperties;

    public IonosEmbeddingGateway(WebClient.Builder webClientBuilder,
                                 IonosProperties ionosProperties) {
        this.ionosProperties = ionosProperties;
        this.webClient = webClientBuilder
                .baseUrl(ionosProperties.getOpenAiBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ionosProperties.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public List<Double> embed(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Cannot create embedding for blank text");
        }

        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("model", ionosProperties.getEmbeddingModel());
        requestBody.put("input", text);
        requestBody.put("encoding_format", "float");

        log.info("Calling IONOS embedding endpoint. model={}, textLength={}",
                ionosProperties.getEmbeddingModel(),
                text.length());

        JsonNode response = webClient.post()
                .uri("/embeddings")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();

        if (response == null) {
            throw new IllegalStateException("IONOS embedding response was null");
        }

        JsonNode embeddingNode = response
                .path("data")
                .path(0)
                .path("embedding");

        if (embeddingNode.isMissingNode() || embeddingNode.isNull()) {
            throw new IllegalStateException("IONOS embedding response does not contain data[0].embedding: " + response);
        }

        if (embeddingNode.isArray()) {
            List<Double> embedding = parseFloatArrayEmbedding(embeddingNode);
            log.info("Received numeric embedding. dimensions={}", embedding.size());
            return embedding;
        }

        if (embeddingNode.isTextual()) {
            String encoded = embeddingNode.asText();
            log.warn("Received textual embedding. Attempting base64 float32 decode. prefix={}",
                    encoded.substring(0, Math.min(40, encoded.length())));

            List<Double> embedding = parseBase64Float32Embedding(encoded);
            log.info("Decoded textual embedding. dimensions={}", embedding.size());
            return embedding;
        }

        throw new IllegalStateException("Unsupported embedding response shape: " + embeddingNode);
    }

    private List<Double> parseFloatArrayEmbedding(JsonNode embeddingNode) {
        List<Double> result = new ArrayList<>();

        for (JsonNode valueNode : embeddingNode) {
            result.add(valueNode.asDouble());
        }

        return result;
    }

    private List<Double> parseBase64Float32Embedding(String encoded) {
        byte[] bytes = Base64.getDecoder().decode(encoded);

        if (bytes.length % Float.BYTES != 0) {
            throw new IllegalStateException(
                    "Base64 embedding byte length is not divisible by 4. byteLength=" + bytes.length
            );
        }

        ByteBuffer buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN);
        List<Double> result = new ArrayList<>(bytes.length / Float.BYTES);

        while (buffer.remaining() >= Float.BYTES) {
            result.add((double) buffer.getFloat());
        }

        return result;
    }
}