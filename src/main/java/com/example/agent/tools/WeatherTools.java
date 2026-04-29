package com.example.agent.tools;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.agent.config.ConfigLoader;
import com.example.agent.config.ModelConfig;
import dev.langchain4j.agent.tool.Tool;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

/**
 * @author Brady (brady@sfmail.sf-express.com)
 * @version 1.0
 * @since 2026/4/28 下午3:09
 **/
@Slf4j
public class WeatherTools {
    private static final HttpClient httpClient = HttpClient.newHttpClient();
    private static final ModelConfig config = ConfigLoader.load(new String[]{});

    @Tool(value = "查询指定经纬度的天气信息, 参数形式为: [经度, 维度], 格式为字符串数组, 返回数据为固定格式Json")
    public String queryWeather(String[] location) {
        if (location == null || location.length != 2) {
            return "天气查询失败";
        }

        String locationStr = location[0] + "," + location[1];
        String requestUrl = String.format("https://api.caiyunapp.com/v2.6/%s/%s/hourly?hourlysteps=1", config.CAI_YUN_TOKEN(), locationStr);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(requestUrl))
                .header("Accept", "application/json")
                .GET()
                .build();
        HttpResponse<String> response = null;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
        } catch (Exception e) {
            log.warn("天气信息查询失败!");
            return "天气查询失败";
        }

        return formatOutput(response.body());
    }

//    public static void main(String[] args) {
//        String[] loc = new String[2];
//        loc[0] = "116.3595";
//        loc[1] = "40.0206";
//
//        String json = queryWeather(loc);
//        String weatherInfo = formatOutput(json);
//        System.out.println(weatherInfo);
//    }

    private static String formatOutput(String rawRes) {
        // 开始解析
        JSONObject root = JSON.parseObject(rawRes);
        JSONObject result = root.getJSONObject("result");
        JSONObject hourly = result.getJSONObject("hourly");

        // 1. 温度 temperature.value
        double temp = hourly.getJSONArray("temperature")
                .getJSONObject(0)
                .getDoubleValue("value");

        // 2. 体感温度 apparent_temperature.value
        double feelTemp = hourly.getJSONArray("apparent_temperature")
                .getJSONObject(0)
                .getDoubleValue("value");

        // 3. 风速 wind.speed
        double windSpeed = hourly.getJSONArray("wind")
                .getJSONObject(0)
                .getDoubleValue("speed");

        // 4. 风向 wind.direction
        double windDir = hourly.getJSONArray("wind")
                .getJSONObject(0)
                .getDoubleValue("direction");

        // 5. 云量
        double cloudRate = hourly.getJSONArray("cloudrate")
                .getJSONObject(0)
                .getDoubleValue("value");

        // 6. 天气描述
        String weatherDesc = hourly.getJSONArray("skycon")
                .getJSONObject(0)
                .getString("value");

        // 输出结果
        System.out.println("温度：" + temp);
        System.out.println("体感温度：" + feelTemp);
        System.out.println("风速：" + windSpeed);
        System.out.println("风向：" + windDir);
        System.out.println("云量：" + cloudRate);
        System.out.println("天气描述：" + weatherDesc);

        String template = "{\"温度\":\"%s\",\"体感温度\":\"%s\",\"风速\":\"%s\",\"风向\":\"%s\",\"云量\":\"%s\",\"天气描述\":\"%s\"}";
        return template.formatted(temp, feelTemp, windSpeed, windDir, cloudRate, weatherDesc);
    }
}
