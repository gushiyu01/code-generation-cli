# Example Project

Spring Boot + MyBatis Plus + PageHelper + Swagger 示例项目

---

## 📁 项目结构

```
example/
├── src/main/java/com/example/project/
│   ├── Application.java               # 启动类
│   ├── common/
│   │   ├── Result.java               # 统一响应结果
│   │   └── PageResult.java           # 分页结果
│   ├── config/
│   │   ├── AppConfig.java            # 业务配置
│   │   ├── MybatisPlusConfig.java    # MyBatis Plus配置
│   │   ├── SwaggerConfig.java         # Swagger配置
│   │   └── WebConfig.java             # Web配置（CORS）
│   ├── controller/
│   │   └── SysUserController.java    # 用户控制器（示例）
│   ├── dto/
│   │   └── SysUserQueryDTO.java      # 查询条件DTO
│   ├── entity/
│   │   └── SysUser.java             # 用户实体
│   ├── exception/
│   │   ├── BusinessException.java    # 业务异常
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   ├── filter/
│   │   └── RequestLoggingFilter.java # 请求日志过滤器
│   ├── mapper/
│   │   ├── SysUserMapper.java        # Mapper接口
│   │   └── SysUserMapper.xml         # Mapper XML
│   ├── service/
│   │   ├── SysUserRepository.java    # Repository接口
│   │   └── impl/
│   │       └── SysUserRepositoryImpl.java # Repository实现
│   └── util/
│       └── StringUtil.java            # 字符串工具类
├── src/main/resources/
│   ├── application.yml                # 配置文件
│   ├── logback-spring.xml             # 日志配置
│   ├── mapper/
│   │   └── *.xml                     # Mapper XML
│   ├── sql/
│   │   └── sys_user.sql              # 建表脚本
│   ├── ValidationMessages.properties  # 校验消息
│   └── ValidationMessages_zh_CN.properties # 中文校验消息
├── src/test/java/                    # 单元测试
├── docker-compose.yml                # Docker Compose
├── Dockerfile                        # Docker构建文件
├── start.sh                          # Linux启动脚本
├── start.bat                         # Windows启动脚本
└── pom.xml                           # Maven配置
```

---

## 🚀 快速开始

### 方式一：IDE运行

1. 导入项目到 IDEA
2. 修改 `application.yml` 中的数据库配置
3. 执行 `sql/sys_user.sql` 创建表
4. 运行 `Application.java`

### 方式二：Jar包运行

```bash
mvn clean package
java -jar target/example-project-1.0.0.jar --spring.profiles.active=dev
```

### 方式三：Docker运行

```bash
docker-compose up -d
```

---

## 🌐 访问地址

| 服务 | 地址 |
|------|------|
| API文档 | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |
| Actuator | http://localhost:8080/actuator |

---

## 📡 API接口

所有接口前缀：`/sysUser`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/sysUser/list` | 分页查询（Body） |
| GET | `/sysUser/get?id=1` | 查询单条 |
| POST | `/sysUser/save` | 新增（Body） |
| POST | `/sysUser/update` | 更新（Body） |
| GET | `/sysUser/delete?id=1` | 删除 |
| POST | `/sysUser/batchDelete` | 批量删除（Body） |

### 请求示例

**分页查询（POST）**
```json
POST /sysUser/list
{
  "id": 1,
  "username": "admin",
  "email": "@example.com",
  "enabled": true,
  "name": "张",
  "orgId": 5,
  "phone": "138",
  "pageNum": 1,
  "pageSize": 10
}
```
> 查询条件说明：
> - `id`、`enabled`、`orgId`：**精确匹配**
> - `username`、`email`、`name`、`phone`：**模糊匹配**（自动加 `%` 通配符）
> - 所有字段可选，不传则不作为查询条件

**新增用户（POST）**
```json
POST /sysUser/save
{
  "username": "newuser",
  "passwordHash": "$2a$10$...",
  "email": "newuser@example.com",
  "name": "新用户",
  "phone": "13900139000",
  "enabled": true,
  "orgId": 3
}
```

**更新用户（POST）**
```json
POST /sysUser/update
{
  "id": 1,
  "username": "admin",
  "email": "admin_new@example.com",
  "name": "张三（已修改）",
  "enabled": true,
  "orgId": 5
}
```

**根据ID查询（GET）**
```
GET /sysUser/get?id=1
```

**删除（GET）**
```
GET /sysUser/delete?id=1
```

**批量删除（POST）**
```json
POST /sysUser/batchDelete
[1, 2, 3]
```

### 响应格式

**成功响应：**
```json
{
  "code": 200,
  "data": { ... }
}
```

**失败响应：**
```json
{
  "code": 500,
  "data": "记录不存在"
}
```

### 字段说明

| 字段 | 类型 | 说明 |
|------|------|------|
| id | Long | 主键，自增 |
| username | String | 用户名 |
| passwordHash | String | 密码（建议存入前加密） |
| email | String | 邮箱 |
| name | String | 真实姓名 |
| phone | String | 手机号 |
| enabled | Boolean | 是否启用 |
| orgId | Long | 所属机构 ID |
| createdAt | LocalDateTime | 创建时间 |
| updatedAt | LocalDateTime | 更新时间 |

---

## 🛡️ 校验规则

| 字段 | 校验注解 |
|------|----------|
| username | @NotBlank, @Size(min=3, max=50) |
| password | @NotBlank, @Size(min=6, max=100) |
| email | @Email |
| phone | @Pattern |
| status | @Min(0), @Max(1) |
| gender | @Min(0), @Max(2) |

---

## 📝 日志查看

```bash
# Linux/macOS
./start.sh logs

# Windows
start.bat logs
```

日志文件位置：`logs/startup.log`

---

## 🧪 运行测试

```bash
mvn test
```

---

## 🔧 配置说明

### 环境配置

```yaml
spring:
  profiles:
    active: dev  # dev | test | prod
```

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/example_db
    username: root
    password: your_password
```

---

## 🐳 Docker说明

### 构建镜像

```bash
docker build -t example-project .
```

### 启动服务

```bash
docker-compose up -d
```

### 停止服务

```bash
docker-compose down
```

---

## 📜 启动脚本命令

```bash
./start.sh          # 启动
./start.sh stop     # 停止
./start.sh restart  # 重启
./start.sh logs     # 查看日志
./start.sh status   # 查看状态
```

---

## 📝 错误码说明

| 错误码 | 说明 |
|--------|------|
| 200 | 成功 |
| 400 | 请求参数错误 |
| 404 | 资源不存在 |
| 500 | 服务器内部错误 |

---

## 📧 联系方式

如有问题，请联系：support@example.com
