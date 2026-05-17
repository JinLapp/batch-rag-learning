package com.example.textvectorizer.ionos.client;

import com.example.textvectorizer.config.IonosProperties;
import com.example.textvectorizer.ionos.dto.IonosUpsertDocumentsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class IonosDocumentCollectionClient {

    private static final Logger log = LoggerFactory.getLogger(IonosDocumentCollectionClient.class);

    private final WebClient webClient;
    private final IonosProperties ionosProperties;

    public IonosDocumentCollectionClient(WebClient.Builder webClientBuilder,
                                         IonosProperties ionosProperties) {
        this.ionosProperties = ionosProperties;
        this.webClient = webClientBuilder
                .baseUrl(ionosProperties.getNativeBaseUrl())
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + ionosProperties.getApiKey())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public void upsertDocuments(IonosUpsertDocumentsRequest request) {
        String collectionId = ionosProperties.getDocumentCollectionId();

        if (ionosProperties.isDryRun()) {
        log.info("DRY RUN: would send {} document(s) to IONOS document collection {}",
                request.getDocuments() == null ? 0 : request.getDocuments().size(),
                collectionId);
        return;
        }

        log.info("Sending {} document(s) to IONOS document collection {}",
                request.getDocuments() == null ? 0 : request.getDocuments().size(),
                collectionId);

        webClient.post()
                .uri("/document-collections/{collectionId}/documents", collectionId)
                .bodyValue(request)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}