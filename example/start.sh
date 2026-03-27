#!/bin/bash

# Example Project Start Script (Linux/macOS)

# 项目名称
APP_NAME="example-project"
# Jar包名称
JAR_NAME="target/example-project-1.0.0.jar"
# JVM参数
JAVA_OPTS="-Xms256m -Xmx512m -XX:+UseG1GC"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}  ${APP_NAME} 启动脚本${NC}"
echo -e "${GREEN}========================================${NC}"

# 检查Java
if ! command -v java &> /dev/null; then
    echo -e "${RED}错误: 未找到Java环境，请先安装JDK 17+${NC}"
    exit 1
fi

java -version

# 检查Jar包
if [ ! -f "$JAR_NAME" ]; then
    echo -e "${YELLOW}警告: 未找到Jar包，正在构建...${NC}"
    mvn clean package -DskipTests
fi

echo -e "${GREEN}启动应用...${NC}"
echo -e "${YELLOW}JVM参数: ${JAVA_OPTS}${NC}"
echo -e "${YELLOW}应用端口: 8080${NC}"
echo -e "${YELLOW}Swagger UI: http://localhost:8080/swagger-ui.html${NC}"
echo -e "${YELLOW}API Docs: http://localhost:8080/v3/api-docs${NC}"
echo ""

# 启动应用
nohup java $JAVA_OPTS -jar $JAR_NAME --spring.profiles.active=dev > logs/startup.log 2>&1 &

# 获取进程ID
APP_PID=$!
echo $APP_PID > logs/app.pid

echo -e "${GREEN}应用已启动，PID: ${APP_PID}${NC}"
echo -e "${GREEN}日志文件: logs/startup.log${NC}"
echo ""

# 等待启动
sleep 5

# 检查进程状态
if ps -p $APP_PID > /dev/null; then
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}  应用启动成功！${NC}"
    echo -e "${GREEN}========================================${NC}"
    echo -e "${YELLOW}访问地址:${NC}"
    echo -e "${YELLOW}  Swagger UI: http://localhost:8080/swagger-ui.html${NC}"
    echo -e "${YELLOW}  API Docs: http://localhost:8080/v3/api-docs${NC}"
else
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}  应用启动失败！${NC}"
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}请查看日志: logs/startup.log${NC}"
    exit 1
fi

# 停止函数
stop() {
    echo -e "${YELLOW}正在停止应用...${NC}"
    if [ -f logs/app.pid ]; then
        PID=$(cat logs/app.pid)
        if ps -p $PID > /dev/null; then
            kill -15 $PID
            sleep 3
            if ps -p $PID > /dev/null; then
                kill -9 $PID
            fi
            echo -e "${GREEN}应用已停止${NC}"
        else
            echo -e "${YELLOW}应用未在运行${NC}"
        fi
        rm -f logs/app.pid
    else
        echo -e "${YELLOW}未找到PID文件${NC}"
    fi
}

# 重启函数
restart() {
    stop
    sleep 2
    echo -e "${YELLOW}正在重启...${NC}"
    ./start.sh
}

# 查看日志
logs() {
    if [ -f logs/startup.log ]; then
        tail -f logs/startup.log
    else
        echo -e "${RED}未找到日志文件${NC}"
    fi
}

# 根据参数执行不同操作
case "$1" in
    stop)
        stop
        ;;
    restart)
        restart
        ;;
    logs)
        logs
        ;;
    status)
        if [ -f logs/app.pid ]; then
            PID=$(cat logs/app.pid)
            if ps -p $PID > /dev/null; then
                echo -e "${GREEN}应用运行中，PID: ${PID}${NC}"
            else
                echo -e "${RED}应用未在运行${NC}"
            fi
        else
            echo -e "${RED}应用未在运行${NC}"
        fi
        ;;
    *)
        echo "用法: ./start.sh {stop|restart|logs|status}"
        ;;
esac
