package com.example.textvectorizer;

import com.example.textvectorizer.config.AppProperties;
import com.example.textvectorizer.config.IonosProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        AppProperties.class,
        IonosProperties.class
})
public class TextVectorizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TextVectorizerApplication.class, args);
    }
}
