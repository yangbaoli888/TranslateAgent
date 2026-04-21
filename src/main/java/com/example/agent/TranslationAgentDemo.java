package com.example.agent;

import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;

import java.util.Scanner;

/**
 * 智能Agent Demo：自动识别语言 + 翻译成英文。
 */
public class TranslationAgentDemo {

    public static void main(String[] args) {
        String apiKey = System.getenv("OPENAI_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("请先设置环境变量 OPENAI_API_KEY");
        }

        OpenAiChatModel model = OpenAiChatModel.builder()
                .apiKey(apiKey)
                .modelName("gpt-4o-mini")
                .temperature(0.0)
                .build();

        LanguageTranslationAgent agent = AiServices.builder(LanguageTranslationAgent.class)
                .chatLanguageModel(model)
                .build();

        System.out.println("=== Language Detection + English Translation Agent Demo ===");
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
