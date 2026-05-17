package com.example.textvectorizer.vectorstore;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PgVectorRepository {

    private final JdbcTemplate jdbcTemplate;

    public PgVectorRepository(JdbcTemplate vectorJdbcTemplate) {
        this.jdbcTemplate = vectorJdbcTemplate;
    }

    public void saveChunk(
            String fileName,
            String sourcePath,
            String checksum,
            int chunkIndex,
            String snippetText,
            String embeddingVector) {

        jdbcTemplate.update("""
                INSERT INTO embedded_chunks (
                    file_name,
                    source_path,
                    checksum,
                    chunk_index,
                    snippet_text,
                    embedding
                )
                VALUES (?, ?, ?, ?, ?, ?::vector)
                """,
                fileName,
                sourcePath,
                checksum,
                chunkIndex,
                snippetText,
                embeddingVector
        );
    }
}