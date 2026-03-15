package com.example.textvectorizer.batch.writer;

import com.example.textvectorizer.domain.ChunkedDocument;
import com.example.textvectorizer.domain.TextChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class LoggingChunkedDocumentWriter implements ItemWriter<ChunkedDocument> {

    private static final Logger log = LoggerFactory.getLogger(LoggingChunkedDocumentWriter.class);

    @Override
    public void write(Chunk<? extends ChunkedDocument> items) {
        log.info("Writer received {} chunked document item(s).", items.size());

        for (ChunkedDocument document : items.getItems()) {
            log.info("Writing chunked document for file: {}, chunkCount={}",
                    document.getFileName(),
                    document.getChunks().size());

            for (TextChunk chunk : document.getChunks()) {
                log.info("Chunk detail: fileName={}, chunkIndex={}, snippetLength={}",
                        document.getFileName(),
                        chunk.getChunkIndex(),
                        chunk.getSnippetText().length());
            }
        }
    }
}