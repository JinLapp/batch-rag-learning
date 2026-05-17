package com.example.textvectorizer.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;

@Component
public class VectorSchemaInitializer {

    private static final Logger log = LoggerFactory.getLogger(VectorSchemaInitializer.class);

    private final DataSource vectorDataSource;

    public VectorSchemaInitializer(
            @Qualifier("vectorDataSource")
            DataSource vectorDataSource) {
        this.vectorDataSource = vectorDataSource;
    }

    @PostConstruct
    public void initialize() {

        try (Connection connection = vectorDataSource.getConnection()) {

            log.info("Initializing PostgreSQL vector schema.");

            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("schema-vector.sql")
            );

            log.info("Vector schema initialization completed.");

        } catch (Exception ex) {
            throw new RuntimeException("Failed to initialize PostgreSQL vector schema", ex);
        }
    }
}