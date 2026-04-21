# Java + Maven + LangChain4j 智能翻译 Agent Demo

这个 Demo 实现了你要求的两个核心功能：

1. 自动识别用户输入语言。
2. 将输入转换为英文（若输入本身是英文，则不翻译、原样返回）。

## 技术栈

- Java 17
- Maven
- LangChain4j
- OpenAI Chat Model（通过 `langchain4j-open-ai`）

## 项目结构

```text
src/main/java/com/example/agent/
├── TranslationAgentDemo.java       # 启动入口（命令行交互）
├── LanguageTranslationAgent.java   # Agent接口 + 系统提示词
└── TranslationResult.java          # 结构化结果对象
```

## 使用步骤

### 1) 配置 OpenAI Key

```bash
export OPENAI_API_KEY="你的key"
```

### 2) 编译

```bash
mvn clean compile
```

### 3) 运行

```bash
mvn exec:java
```

## 运行示例

输入：

```text
你好，今天心情不错。
```

输出（示意）：

```text
Detected language : Chinese
English output    : Hello, I'm in a good mood today.
```

如果输入：

```text
How are you?
```

则输出中 `English output` 会保持原句（不改写）。

## 说明

- 这个 Demo 采用了 `temperature=0.0`，尽量保证稳定输出。
- Agent 通过系统提示词被约束为返回结构化 JSON，以便映射成 `TranslationResult`。
