FROM openjdk:17-jdk-slim AS builder

WORKDIR /app

# 复制Maven相关文件
COPY pom.xml .
COPY src ./src

# 设置Maven镜像加速
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories

# 安装Maven和必要工具
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# 打包项目
RUN mvn clean package -DskipTests

# 运行阶段
FROM openjdk:17-jre-slim

WORKDIR /app

# 创建日志目录
RUN mkdir -p /app/logs

# 从构建阶段复制jar包
COPY --from=builder /app/target/*.jar app.jar

# 暴露端口
EXPOSE 8080

# 环境变量
ENV JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
