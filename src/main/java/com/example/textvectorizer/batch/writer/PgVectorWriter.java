package com.example.textvectorizer.batch.writer;

import com.example.textvectorizer.domain.EmbeddedChunk;
import com.example.textvectorizer.domain.EmbeddedDocument;
import com.example.textvectorizer.metadata.service.ImportHistoryService;
import com.example.textvectorizer.vectorstore.PgVectorRepository;
import com.example.textvectorizer.vectorstore.VectorFormattingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class PgVectorWriter implements ItemWriter<EmbeddedDocument> {

    private static final Logger log = LoggerFactory.getLogger(PgVectorWriter.class);

    private final PgVectorRepository repository;
    private final ImportHistoryService importHistoryService;

    public PgVectorWriter(PgVectorRepository repository,
                          ImportHistoryService importHistoryService) {
        this.repository = repository;
        this.importHistoryService = importHistoryService;
    }

    @Override
    public void write(Chunk<? extends EmbeddedDocument> items) {

        for (EmbeddedDocument document : items.getItems()) {

            for (EmbeddedChunk chunk : document.getEmbeddedChunks()) {

                repository.saveChunk(
                        chunk.getFileName(),
                        chunk.getSourcePath(),
                        chunk.getChecksum(),
                        chunk.getChunkIndex(),
                        chunk.getSnippetText(),
                        VectorFormattingUtils.toPgVector(chunk.getEmbedding())
                );
            }

            importHistoryService.markAsImported(
                    document.getFileName(),
                    document.getSourcePath(),
                    document.getChecksum()
            );

            log.info(
                    "Stored document in PostgreSQL vector store: fileName={}, chunkCount={}",
                    document.getFileName(),
                    document.getEmbeddedChunks().size()
            );
        }
    }
}