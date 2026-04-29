package com.example.agent;

import com.example.agent.config.ConfigLoader;
import com.example.agent.config.ModelConfig;
import com.example.agent.tools.AgentTools;
import com.example.agent.tools.WeatherTools;
import com.example.agent.tools.WebSearchTool;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;

/**
 * 通用对话Agent Demo：支持聊天 + 联网工具 + 本地文件读取工具。
 */
public class ChatAgentDemo {

    public static void main(String[] args) {
        ModelConfig config = ConfigLoader.load(args);

        ChatLanguageModel model = OpenAiChatModel.builder()
                .baseUrl(config.baseUrl())
                .apiKey(config.apiKey())
                .modelName(config.modelName())
                .temperature(config.temperature())
                .build();

        GeneralAssistantAgent agent = AiServices.builder(GeneralAssistantAgent.class)
                .chatLanguageModel(model)
                .tools(new AgentTools(), new WeatherTools(), new WebSearchTool())
                .build();

        System.out.println("=== Generic Chat Agent Demo (with web + file tools) ===");
        System.out.println("Model: " + config.modelName());
        System.out.println("请输入问题（输入 exit 退出）：");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n> ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input.trim())) {
                    System.out.println("Bye!");
                    break;
                }

                String answer = agent.chat(input);
                System.out.println("\nAssistant:\n" + answer);
            }
        }
    }
}
