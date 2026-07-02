# Yufan AI 智能助手项目

基于 Spring Boot 3.4 + Spring AI 构建的智能AI助手应用，支持本地大模型部署和云端API调用，提供课程预约、智能客服、PDF文档问答等功能。

## 技术栈

- **语言**: Java 17
- **框架**: Spring Boot 3.4.3
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis Plus 3.5.10.1
- **AI框架**: Spring AI 1.0.0-M6
- **工具**: Lombok 1.18.22

## 核心功能

### 1. 智能聊天
- 支持本地 Ollama 模型（Qwen3:4B、DeepSeek-R1）
- 支持阿里云 DashScope OpenAI 兼容接口（Qwen-Turbo）

### 2. 课程预约系统
- 学校管理
- 课程管理
- 课程预约与查询

### 3. PDF 文档问答
- 支持上传 PDF 文件
- 基于向量检索的文档问答

### 4. 智能客服
- 基于知识库的自动回复

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.8+
- MySQL 8.0+
- Ollama（可选，用于本地模型）

### 配置说明

修改 `src/main/resources/application.yaml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yufan?serverTimezone=Asia/Shanghai
    username: your_username
    password: your_password
  ai:
    ollama:
      base-url: http://localhost:11434
      chat:
        model: qwen3:4b
    openai:
      api-key: your_api_key
```

### 启动方式

```bash
# 1. 启动 Ollama（可选）
ollama run qwen3:4b

# 2. 编译项目
mvn clean package

# 3. 运行
java -jar target/heima-ai-0.0.1-SNAPSHOT.jar
```

## API 接口

### 聊天接口
- `POST /api/chat` - 智能聊天

### 课程管理
- `GET /api/courses` - 查询课程列表
- `POST /api/courses` - 创建课程
- `GET /api/courses/{id}` - 查询课程详情

### 课程预约
- `POST /api/reservations` - 预约课程
- `GET /api/reservations` - 查询预约记录

### PDF 文档
- `POST /api/pdf/upload` - 上传 PDF
- `POST /api/pdf/chat` - PDF 问答

## 项目结构
