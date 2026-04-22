package com.example.agent;

import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.TokenStream;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;

/**
 * 通用对话Agent Demo：支持聊天 + 联网工具 + 本地文件读取工具（流式输出）。
 */
public class ChatAgentDemo {

    public static void main(String[] args) throws InterruptedException {
        ModelConfig config = ConfigLoader.load(args);

        OpenAiStreamingChatModel model = OpenAiStreamingChatModel.builder()
                .baseUrl(config.baseUrl())
                .apiKey(config.apiKey())
                .modelName(config.modelName())
                .temperature(config.temperature())
                .build();

        GeneralAssistantAgent agent = AiServices.builder(GeneralAssistantAgent.class)
                .streamingChatLanguageModel(model)
                .tools(new AgentTools())
                .build();

        System.out.println("=== Generic Chat Agent Demo (with web + file tools, streaming) ===");
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

                CountDownLatch done = new CountDownLatch(1);
                TokenStream stream = agent.chat(input);

                System.out.println("\nAssistant:");
                stream.onPartialResponse(System.out::print)
                        .onCompleteResponse(ignored -> {
                            System.out.println();
                            done.countDown();
                        })
                        .onError(error -> {
                            System.err.println("\n[stream error] " + error.getMessage());
                            done.countDown();
                        })
                        .start();

                done.await();
            }
        }
    }
}
