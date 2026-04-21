package com.example.agent;

import dev.langchain4j.agent.tool.Tool;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class AgentTools {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Tool("使用 DuckDuckGo 即时答案接口进行简单联网搜索")
    public String searchWeb(String query) {
        try {
            String encoded = URLEncoder.encode(query, StandardCharsets.UTF_8);
            String url = "https://api.duckduckgo.com/?q=" + encoded + "&format=json&no_html=1&skip_disambig=1";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("Accept", "application/json")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return trim("DuckDuckGo raw response:\n" + response.body(), 3000);
        } catch (Exception e) {
            return "联网搜索失败: " + e.getMessage();
        }
    }

    @Tool("获取指定网页URL内容，适合在已知链接时联网读取")
    public String fetchUrl(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", "TranslateAgentDemo/1.0")
                    .GET()
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return trim("URL: " + url + "\nHTTP: " + response.statusCode() + "\nBody:\n" + response.body(), 4000);
        } catch (Exception e) {
            return "读取URL失败: " + e.getMessage();
        }
    }

    @Tool("读取本地文件内容，参数为文件路径")
    public String readLocalFile(String filePath) {
        try {
            Path path = Path.of(filePath).normalize();
            if (!Files.exists(path)) {
                return "文件不存在: " + path;
            }
            if (!Files.isReadable(path)) {
                return "文件不可读: " + path;
            }

            String content = Files.readString(path);
            return trim("File: " + path + "\n" + content, 4000);
        } catch (IOException e) {
            return "读取文件失败: " + e.getMessage();
        }
    }

    private String trim(String text, int maxLen) {
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "\n...<truncated>";
    }
}
