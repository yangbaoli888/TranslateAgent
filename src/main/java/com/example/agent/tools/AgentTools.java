package com.example.agent.tools;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.example.agent.config.ConfigLoader;
import com.example.agent.config.ModelConfig;
import dev.langchain4j.agent.tool.Tool;
import opennlp.tools.util.StringUtil;

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
    private static final ModelConfig config = ConfigLoader.load(new String[]{});

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

    @Tool("查询订单信息，参数为订单号")
    public String queryOrderInfo(String orderNumber) {
        if (StringUtil.isEmpty(orderNumber)) {
            return "";
        }

        String baseUrl = "https://admin.sjims.com/oms/web/customerOrder/detail?businessOrderNumber=";
        String queryUrl = baseUrl + URLEncoder.encode(orderNumber, StandardCharsets.UTF_8);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(queryUrl))
                .header("Accept", "application/json")
                .header("Cookie", config.YOOHOO_COOKIE())
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "订单数据查询失败";
        }

        return response.body();
    }

    @Tool("获取当前位置的当前时间, 时间格式为'yyyy-MM-dd HH:mm:ss'")
    public String currentTime() {
        DateTime now = DateTime.now();
        return DateUtil.formatDateTime(now);
    }

    private String trim(String text, int maxLen) {
        if (text.length() <= maxLen) {
            return text;
        }
        return text.substring(0, maxLen) + "\n...<truncated>";
    }
}
