# Java + Maven + LangChain4j 智能 Agent Demo

本项目现在包含两个 Agent Demo：

1. **翻译 Agent**：自动识别输入语言，并将输入转换成英文（若原文是英文则保持原样）。
2. **通用对话 Agent**：支持多轮对话，具备联网工具与本地文件读取能力，且回复为流式输出。

> 两个 Demo 都支持通过配置文件自定义模型（OpenAI-Compatible 协议），并内置 DeepSeek 配置支持。

## 技术栈

- Java 17
- Maven
- LangChain4j

## 项目结构

```text
src/main/java/com/example/agent/
├── TranslationAgentDemo.java       # 翻译 Agent 入口
├── ChatAgentDemo.java              # 通用对话 Agent 入口
├── LanguageTranslationAgent.java   # 翻译 Agent 接口
├── GeneralAssistantAgent.java      # 通用对话 Agent 接口
├── AgentTools.java                 # 联网与文件工具
├── TranslationResult.java          # 翻译结构化结果
├── ConfigLoader.java               # 配置读取
└── ModelConfig.java                # 模型配置对象

src/main/resources/
└── agent.properties                # 默认模型配置模板
```

## 配置文件

默认读取顺序：

1. 启动参数传入路径（如 `config/my-model.properties`）
2. 若没有则读取 `config/agent.properties`
3. 如果也不存在，则回退到 classpath 的 `agent.properties`

配置示例：

```properties
model.provider=openai
model.base-url=https://api.openai.com/v1
model.api-key=${OPENAI_API_KEY}
model.name=gpt-4o-mini
model.temperature=0.0
```

`model.api-key` 支持 `${ENV_NAME}` 格式，从环境变量读取。

新增 `model.provider`（可选，默认 `openai`）：

- `openai`：默认 base-url 为 `https://api.openai.com/v1`
- `deepseek`：默认 base-url 为 `https://api.deepseek.com/v1`

当你显式配置了 `model.base-url` 时，会优先使用你填的地址。

## 使用步骤


DeepSeek 配置示例：

```properties
model.provider=deepseek
model.api-key=${DEEPSEEK_API_KEY}
model.name=deepseek-chat
model.temperature=0.3
```

### 1) 配置环境变量

```bash
export OPENAI_API_KEY="你的key"
# 或
export DEEPSEEK_API_KEY="你的key"
```

### 2) （可选）创建外部配置文件

```bash
mkdir -p config
cat > config/agent.properties <<'CONF'
model.provider=openai
model.base-url=https://api.openai.com/v1
model.api-key=${OPENAI_API_KEY}
model.name=gpt-4o-mini
model.temperature=0.0
CONF
```

### 3) 编译

```bash
mvn clean compile
```

## 运行 Demo

### A. 运行翻译 Agent

```bash
mvn exec:java -Dexec.mainClass="com.example.agent.TranslationAgentDemo"
```

指定配置文件：

```bash
mvn exec:java -Dexec.mainClass="com.example.agent.TranslationAgentDemo" -Dexec.args="config/agent.properties"
```

### B. 运行通用对话 Agent（流式输出）

```bash
mvn exec:java -Dexec.mainClass="com.example.agent.ChatAgentDemo"
```

指定配置文件：

```bash
mvn exec:java -Dexec.mainClass="com.example.agent.ChatAgentDemo" -Dexec.args="config/agent.properties"
```

## 通用对话 Agent 的工具能力

`AgentTools` 提供以下工具：

1. `searchWeb(query)`：调用 DuckDuckGo 即时答案接口进行联网搜索。
2. `fetchUrl(url)`：读取指定 URL 的页面内容。
3. `readLocalFile(filePath)`：读取本地文件内容。

> 提示：如果你让 Agent “读取某个本地文件并总结”，它会自动调用 `readLocalFile`。

聊天模式下回复会按 token 流式打印到终端。
