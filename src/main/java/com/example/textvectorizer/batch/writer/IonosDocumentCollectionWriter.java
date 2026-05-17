package com.example.textvectorizer.batch.writer;

import com.example.textvectorizer.domain.EmbeddedChunk;
import com.example.textvectorizer.domain.EmbeddedDocument;
import com.example.textvectorizer.ionos.client.IonosDocumentCollectionClient;
import com.example.textvectorizer.ionos.dto.IonosDocumentRequest;
import com.example.textvectorizer.ionos.dto.IonosUpsertDocumentsRequest;
import com.example.textvectorizer.metadata.service.ImportHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class IonosDocumentCollectionWriter implements ItemWriter<EmbeddedDocument> {

    private static final Logger log = LoggerFactory.getLogger(IonosDocumentCollectionWriter.class);

    private final IonosDocumentCollectionClient ionosClient;
    private final ImportHistoryService importHistoryService;

    public IonosDocumentCollectionWriter(IonosDocumentCollectionClient ionosClient,
                                         ImportHistoryService importHistoryService) {
        this.ionosClient = ionosClient;
        this.importHistoryService = importHistoryService;
    }

    @Override
    public void write(Chunk<? extends EmbeddedDocument> items) {
        log.info("Writer received {} embedded document item(s).", items.size());

        for (EmbeddedDocument document : items.getItems()) {
            List<IonosDocumentRequest> requests = new ArrayList<>();

            for (EmbeddedChunk chunk : document.getEmbeddedChunks()) {
                Map<String, Object> metadata = new LinkedHashMap<>();
                metadata.put("fileName", chunk.getFileName());
                metadata.put("sourcePath", chunk.getSourcePath());
                metadata.put("checksum", chunk.getChecksum());
                metadata.put("chunkIndex", chunk.getChunkIndex());
                metadata.put("discoveredAt", chunk.getDiscoveredAt().toString());

                requests.add(new IonosDocumentRequest(
                        chunk.getSnippetText(),
                        chunk.getEmbedding(),
                        metadata
                ));
            }

            ionosClient.upsertDocuments(new IonosUpsertDocumentsRequest(requests));

            importHistoryService.markAsImported(
                    document.getFileName(),
                    document.getSourcePath(),
                    document.getChecksum()
            );

            log.info("Successfully persisted and marked imported: fileName={}, chunkCount={}",
                    document.getFileName(),
                    document.getEmbeddedChunks().size());
        }
    }
}