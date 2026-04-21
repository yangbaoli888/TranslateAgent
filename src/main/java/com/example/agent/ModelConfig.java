package com.example.agent;

public record ModelConfig(
        String baseUrl,
        String apiKey,
        String modelName,
        Double temperature
) {
}
