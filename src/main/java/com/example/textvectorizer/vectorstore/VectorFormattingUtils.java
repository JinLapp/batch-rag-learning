package com.example.textvectorizer.vectorstore;

import java.util.List;
import java.util.stream.Collectors;

public class VectorFormattingUtils {

    private VectorFormattingUtils() {
    }

    public static String toPgVector(List<Double> embedding) {
        return "[" +
                embedding.stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(","))
                + "]";
    }
}