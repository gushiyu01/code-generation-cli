@echo off
REM Example Project Start Script (Windows)

setlocal enabledelayedexpansion

set APP_NAME=example-project
set JAR_NAME=target\example-project-1.0.0.jar
set JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC

echo ========================================
echo   %APP_NAME% 启动脚本
echo ========================================

echo.

REM 检查Java
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Java环境，请先安装JDK 17+
    exit /b 1
)

REM 检查Jar包
if not exist "%JAR_NAME%" (
    echo 警告: 未找到Jar包，正在构建...
    call mvn clean package -DskipTests
)

echo 启动应用...
echo JVM参数: %JAVA_OPTS%
echo 应用端口: 8080
echo Swagger UI: http://localhost:8080/swagger-ui.html
echo.

REM 创建logs目录
if not exist "logs" mkdir logs

REM 启动应用
set STARTUP_LOG=logs\startup.log
start /b java %JAVA_OPTS% -jar %JAR_NAME% --spring.profiles.active=dev > %STARTUP_LOG% 2>&1

echo 应用已启动
echo 日志文件: %STARTUP_LOG%
echo.

REM 等待启动
timeout /t 5 /nobreak >nul

REM 检查进程
tasklist /fi "imagename eq java.exe" | findstr /i "java" >nul
if errorlevel 1 (
    echo ========================================
    echo   应用启动失败！
    echo ========================================
    echo 请查看日志: %STARTUP_LOG%
    exit /b 1
) else (
    echo ========================================
    echo   应用启动成功！
    echo ========================================
    echo 访问地址:
    echo   Swagger UI: http://localhost:8080/swagger-ui.html
    echo   API Docs: http://localhost:8080/v3/api-docs
    echo.
    echo ========================================
    echo   命令说明:
    echo ========================================
    echo   stop    - 停止应用
    echo   restart - 重启应用
    echo   logs    - 查看日志
    echo   status  - 查看状态
    echo ========================================
)

goto :end

:stop
echo 正在停止应用...
taskkill /f /im java.exe >nul 2>&1
echo 应用已停止
goto :end

:restart
echo 正在重启应用...
call start.bat
goto :end

:logs
if exist "%STARTUP_LOG%" (
    type "%STARTUP_LOG%"
) else (
    echo 未找到日志文件
)
goto :end

:status
tasklist /fi "imagename eq java.exe" | findstr /i "java" >nul
if errorlevel 1 (
    echo 应用未在运行
) else (
    echo 应用运行中
)
goto :end

:end
