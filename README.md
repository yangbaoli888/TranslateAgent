# Java + Maven + LangChain4j 智能翻译 Agent Demo

这个 Demo 实现两个核心功能：

1. 自动识别用户输入语言。
2. 将输入转换为英文（若输入本身是英文，则不翻译、原样返回）。

并且支持**通过配置文件自定义模型**（基于 OpenAI-Compatible 协议，可接不同模型服务）。

## 技术栈

- Java 17
- Maven
- LangChain4j

## 项目结构

```text
src/main/java/com/example/agent/
├── TranslationAgentDemo.java       # 启动入口（命令行交互）
├── LanguageTranslationAgent.java   # Agent接口 + 系统提示词
├── TranslationResult.java          # 结构化结果对象
├── ConfigLoader.java               # 配置文件读取
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
model.base-url=https://api.openai.com/v1
model.api-key=${OPENAI_API_KEY}
model.name=gpt-4o-mini
model.temperature=0.0
```

> `model.api-key` 支持 `${ENV_NAME}` 格式，从环境变量读取。

## 使用步骤

### 1) 配置环境变量（示例）

```bash
export OPENAI_API_KEY="你的key"
```

### 2) （可选）创建外部配置文件

```bash
mkdir -p config
cat > config/agent.properties <<'CONF'
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

### 4) 运行

- 使用默认读取路径：

```bash
mvn exec:java
```

- 指定配置文件路径：

```bash
mvn exec:java -Dexec.args="config/agent.properties"
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

则 `English output` 会保持原句。
