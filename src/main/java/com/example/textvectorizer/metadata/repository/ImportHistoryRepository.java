package com.example.textvectorizer.metadata.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ImportHistoryRepository {

    private final JdbcTemplate jdbcTemplate;

    public ImportHistoryRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean existsByChecksum(String checksum) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM import_history WHERE checksum = ?",
                Integer.class,
                checksum
        );
        return count != null && count > 0;
    }

    public void save(String fileName, String sourcePath, String checksum) {
        jdbcTemplate.update(
                "INSERT INTO import_history (file_name, source_path, checksum, imported_at) VALUES (?, ?, ?, CURRENT_TIMESTAMP)",
                fileName,
                sourcePath,
                checksum
        );
    }
}