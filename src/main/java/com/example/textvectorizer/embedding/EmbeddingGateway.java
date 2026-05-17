package com.example.textvectorizer.embedding;

import java.util.List;

public interface EmbeddingGateway {

    List<Double> embed(String text);
}