package com.example.agent.tools;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.example.agent.ConfigLoader;
import com.example.agent.ModelConfig;
import dev.langchain4j.agent.tool.Tool;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author Brady (brady@sfmail.sf-express.com)
 * @version 1.0
 * @since 2026/4/28 下午5:46
 **/
public class WebSearchTool {

    private static final HttpClient httpClient = HttpClient.newBuilder().build();
    private static final ModelConfig config = ConfigLoader.load(new String[]{});

    @Tool("搜索引擎工具, 可以通过关键字在互联网上检索内容")
    public String searchInternet(String question) {
        if (StrUtil.isBlank(question)) {
            return StrUtil.EMPTY;
        }

        String reqBody = "{\"query\":\"%s\",\"include_answer\":\"advanced\",\"search_depth\":\"advanced\"}";

        String tavilyToken = config.TAVILY_TOKEN();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://api.tavily.com/search"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + tavilyToken)
                .POST(HttpRequest.BodyPublishers.ofString(reqBody.formatted(question)))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            return "搜索引擎查询失败";
        }

        String body = response.body();
        return JSON.parseObject(body).getString("answer");
    }

//    public static void main(String[] args) {
//        String q = "布洛芬的作用";
//        String s = searchInternet(q);
//        System.out.println(s);
//    }
}
