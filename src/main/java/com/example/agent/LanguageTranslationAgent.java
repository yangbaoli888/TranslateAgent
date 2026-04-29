package com.example.agent;

import com.example.agent.pojo.TranslationResult;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

public interface LanguageTranslationAgent {

    @SystemMessage("""
            你是一个语言识别与翻译Agent。
            任务：
            1) 识别用户输入文本的语言，输出语言的英文名称（例如 Chinese, English, Japanese）。
            2) 把输入文本转换成英文；如果原文已经是英文，不要改写，原样返回。

            返回格式必须是严格JSON，不要输出任何额外文本：
            {
              "sourceLanguage": "<language in English>",
              "translatedText": "<english text>"
            }
            """)
    TranslationResult process(@UserMessage String userInput);
}
