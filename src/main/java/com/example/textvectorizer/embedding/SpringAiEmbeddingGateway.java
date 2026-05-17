package com.example.textvectorizer.embedding;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

//@Component jli260503 - Commented out to allow switching between different embedding gateway implementations without needing to change the code. The active implementation can be selected via configuration or by commenting/uncommenting the @Component annotation on the desired implementation class. 
public class SpringAiEmbeddingGateway implements EmbeddingGateway {

    private static final Logger log = LoggerFactory.getLogger(SpringAiEmbeddingGateway.class);

    private final EmbeddingModel embeddingModel;

    public SpringAiEmbeddingGateway(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @Override
    public List<Double> embed(String text) {
        log.info("Generating embedding for text length={}", text == null ? 0 : text.length());



        float[] embedding = embeddingModel.embed(text);

        return toDoubleList(embedding);
    }

    private List<Double> toDoubleList(float[] values) {
        List<Double> result = new ArrayList<>(values.length);
        for (float value : values) {
            result.add((double) value);
        }
        return result;
    }
}