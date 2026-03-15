package com.example.textvectorizer.metadata.service;

import com.example.textvectorizer.metadata.repository.ImportHistoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ImportHistoryService {

    private static final Logger log = LoggerFactory.getLogger(ImportHistoryService.class);

    private final ImportHistoryRepository repository;

    public ImportHistoryService(ImportHistoryRepository repository) {
        this.repository = repository;
    }

    public boolean isAlreadyImported(String checksum) {
        boolean exists = repository.existsByChecksum(checksum);
        log.debug("Duplicate check for checksum [{}]: {}", checksum, exists);
        return exists;
    }

    public void markAsImported(String fileName, String sourcePath, String checksum) {
        repository.save(fileName, sourcePath, checksum);
        log.info("Recorded file in import history: fileName={}, sourcePath={}, checksum={}",
                fileName, sourcePath, checksum);
    }
}