# spring ai demo 开发

## 开发环境

JDK >17
下载地址：https://www.openlogic.com/openjdk-downloads
这里选择jdk 21

## 大模型选择

### 1. 阿里通义千问

### 2. deepseek

### 3. ollama

下载安装【下载地址：https://ollama.com/download/windows】

- 版本查看

```agsl
ollama --version
```

- 安装

```
ollama run deepseek-r1:8b
```

- test

```
>>>who are you
```

- 备注
  如果电脑配置可以，建议优先选取更大的模型。一般家用电脑可以在 1.5b、7b、8b 中选一个，如果配置比较好，可以尝试运行 14b、32b 甚至
  70b 模型，越大效果越好。
  https://github.com/springaialibaba/spring-ai-alibaba-examples/blob/main/spring-ai-alibaba-video-example/dashscope-video/src/main/java/com/alibaba/cloud/ai/example/video/VideoController.java

## 开发



