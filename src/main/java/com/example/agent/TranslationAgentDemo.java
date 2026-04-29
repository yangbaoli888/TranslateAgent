package com.example.agent;

import com.example.agent.config.ConfigLoader;
import com.example.agent.config.ModelConfig;
import com.example.agent.pojo.TranslationResult;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;

/**
 * 智能Agent Demo：自动识别语言 + 翻译成英文。
 */
public class TranslationAgentDemo {

    public static void main(String[] args) {
        ModelConfig config = ConfigLoader.load(args);

        ChatLanguageModel model = OpenAiChatModel.builder()
                .baseUrl(config.baseUrl())
                .apiKey(config.apiKey())
                .modelName(config.modelName())
                .temperature(config.temperature())
                .build();

        LanguageTranslationAgent agent = AiServices.builder(LanguageTranslationAgent.class)
                .chatLanguageModel(model)
                .build();

        System.out.println("=== Generic LLM Translation Agent Demo ===");
        System.out.println("Model: " + config.modelName());
        System.out.println("请输入任意语言文本（输入 exit 退出）：");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n> ");
                String input = scanner.nextLine();

                if ("exit".equalsIgnoreCase(input.trim())) {
                    System.out.println("Bye!");
                    break;
                }

                TranslationResult result = agent.process(input);
                System.out.println("Detected language : " + result.sourceLanguage());
                System.out.println("English output    : " + result.translatedText());
            }
        }
    }
}
