package com.example.agent;

/**
 * Agent返回的结构化结果。
 *
 * @param sourceLanguage 检测到的输入语言（英文名称）
 * @param translatedText 英文结果（若原文已是英文，则保持原文）
 */
public record TranslationResult(String sourceLanguage, String translatedText) {
}
