package com.example.agent;

public record ModelConfig(
        String baseUrl,
        String apiKey,
        String modelName,
        Double temperature,
        String CAI_YUN_TOKEN,
        String TAVILY_TOKEN,
        String YOOHOO_COOKIE
) {
}
