package com.example.agent.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ConfigLoader {

    private ConfigLoader() {
    }

    public static ModelConfig load(String[] args) {
        String path = args.length > 0 ? args[0] : "config/agent.properties";

        Properties properties = new Properties();
        Path externalPath = Path.of(path);

        if (Files.exists(externalPath)) {
            try (InputStream inputStream = Files.newInputStream(externalPath)) {
                properties.load(inputStream);
                return toConfig(properties);
            } catch (IOException e) {
                throw new IllegalStateException("读取配置文件失败: " + externalPath, e);
            }
        }

        try (InputStream inputStream = ConfigLoader.class.getClassLoader().getResourceAsStream("agent.properties")) {
            if (inputStream == null) {
                throw new IllegalStateException("未找到配置文件，请创建 " + externalPath + " 或 classpath:agent.properties");
            }
            properties.load(inputStream);
            return toConfig(properties);
        } catch (IOException e) {
            throw new IllegalStateException("读取 classpath 配置文件失败", e);
        }
    }

    private static ModelConfig toConfig(Properties properties) {
        String baseUrl = resolveValue(require(properties, "model.base-url"));
        String apiKey = resolveValue(require(properties, "model.api-key"));
        String modelName = resolveValue(require(properties, "model.name"));
        Double temperature = Double.parseDouble(properties.getProperty("model.temperature", "0.0"));

        String caiYunToken = System.getenv("CAI_YUN_TOKEN");
        String tavilyToken = System.getenv("TAVILY_TOKEN");
        String yoohooCookie = System.getenv("yoohoo_cookie");
        return new ModelConfig(baseUrl, apiKey, modelName, temperature, caiYunToken, tavilyToken, yoohooCookie);
    }

    private static String resolveValue(String value) {
        if (value.startsWith("${") && value.endsWith("}")) {
            String envKey = value.substring(2, value.length() - 1);
            String envValue = System.getenv(envKey);
            if (envValue == null || envValue.isBlank()) {
                throw new IllegalStateException("环境变量未设置: " + envKey);
            }
            return envValue.trim();
        }
        return value;
    }

    private static String require(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException("缺少必填配置: " + key);
        }
        return value.trim();
    }
}
