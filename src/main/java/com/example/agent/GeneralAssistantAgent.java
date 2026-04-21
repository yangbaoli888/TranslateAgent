package com.example.agent;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface GeneralAssistantAgent {

    @SystemMessage("""
            你是一个通用对话助手。
            - 你可以与用户自然对话。
            - 当用户问题涉及实时信息时，优先调用联网工具。
            - 当用户要求查看本地内容时，调用本地文件读取工具。
            - 回答时请明确你是否使用了工具，以及关键结论。
            """)
    String chat(@UserMessage String message);
}
