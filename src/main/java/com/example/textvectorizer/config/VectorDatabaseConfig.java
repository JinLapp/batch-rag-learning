package com.example.textvectorizer.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class VectorDatabaseConfig {

    @Bean
    @ConfigurationProperties(prefix = "vector-db")
    public DataSource vectorDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate vectorJdbcTemplate(
            @Qualifier("vectorDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}