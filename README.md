# Code Generation CLI

基于数据库表结构自动生成 Spring Boot + MyBatis Plus 项目代码的工具。

---

## 📁 项目结构

```
code-generation-cli/
├── codegen/                    # 代码生成器
│   ├── src/main/java/com/example/codegen/
│   │   ├── CodeGeneratorApplication.java  # 启动入口
│   │   ├── config/
│   │   │   └── CodeGeneratorConfig.java   # 配置类
│   │   ├── generator/
│   │   │   └── CodeGenerator.java        # 生成器核心
│   │   ├── model/
│   │   │   ├── ColumnInfo.java            # 列信息
│   │   │   └── TableInfo.java             # 表信息
│   │   └── util/
│   │       ├── DbUtil.java               # 数据库工具
│   │       └── StringUtil.java           # 字符串工具
│   └── pom.xml
├── example/                    # 生成代码的示例项目
│   ├── src/main/java/com/example/project/
│   │   ├── controller/         # 控制器
│   │   ├── entity/             # 实体类
│   │   ├── mapper/             # Mapper接口
│   │   ├── service/            # 服务层
│   │   └── ...
│   ├── src/main/resources/
│   │   ├── mapper/             # Mapper XML
│   │   └── sql/                # 建表脚本
│   ├── docker-compose.yml
│   ├── Dockerfile
│   └── pom.xml
└── pom.xml                     # 父POM
```

---

## 🚀 快速开始

### 第一步：配置数据库连接

编辑 `codegen/src/main/java/com/example/codegen/CodeGeneratorApplication.java`：

```java
config.setJdbcUrl("jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
config.setUsername("root");
config.setPassword("your_password");
config.setBasePackage("com.example.project");
config.setOutputPath("your_output_path");  // 代码输出目录
```

### 第二步：选择生成模式

```java
// 【模式一】单表生成
config.setTableName("sys_user");

// 【模式二】批量生成（按表名前缀）
config.setTablePrefix("sys_");

// 【模式三】指定多张表（逗号分隔）
config.setTables("sys_user,sys_role,sys_menu");
```

### 第三步：运行生成器

```bash
cd codegen
mvn clean compile
mvn exec:java -Dexec.mainClass="com.example.codegen.CodeGeneratorApplication"
```

---

## 📦 生成内容

代码生成器会根据数据库表结构自动生成以下内容：

| 层次 | 文件 |
|------|------|
| Entity | 实体类（带字段注释） |
| Mapper | MyBatis Plus Mapper 接口 |
| Mapper XML | MyBatis XML 映射文件 |
| Repository | 数据访问层接口 |
| Repository Impl | 数据访问层实现 |
| Service | 业务层接口 |
| Service Impl | 业务层实现 |
| Controller | REST 控制器 |
| DTO | 查询条件对象 |

---

## 📋 生成代码示例

### Entity
```java
public class SysUser {
    private Long id;
    private String username;
    private String passwordHash;
    private String email;
    private String name;
    private String phone;
    private Boolean enabled;
    private Long orgId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
```

### Controller
```java
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    
    @PostMapping("/list")
    public Result<PageResult<SysUser>> list(@RequestBody SysUserQueryDTO query) {
        // 分页查询
    }
    
    @GetMapping("/get")
    public Result<SysUser> get(@RequestParam Long id) {
        // 根据ID查询
    }
    
    @PostMapping("/save")
    public Result<Void> save(@RequestBody SysUser sysUser) {
        // 新增
    }
    
    @PostMapping("/update")
    public Result<Void> update(@RequestBody SysUser sysUser) {
        // 更新
    }
    
    @GetMapping("/delete")
    public Result<Void> delete(@RequestParam Long id) {
        // 删除
    }
}
```

---

## 🐳 运行示例项目

```bash
cd example

# 修改配置
# 编辑 src/main/resources/application.yml 中的数据库配置

# 构建
mvn clean package

# 运行
java -jar target/example-project-1.0.0.jar
```

或使用 Docker：

```bash
docker-compose up -d
```

---

## 🌐 访问地址

| 服务 | 地址 |
|------|------|
| API文档 | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

---

## 🔧 API 接口

所有接口前缀：`/sysUser`

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/sysUser/list` | 分页查询 |
| GET | `/sysUser/get?id=1` | 根据ID查询 |
| POST | `/sysUser/save` | 新增 |
| POST | `/sysUser/update` | 更新 |
| GET | `/sysUser/delete?id=1` | 删除 |
| POST | `/sysUser/batchDelete` | 批量删除 |

---

## 📝 配置说明

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_database
    username: root
    password: your_password
```

### MyBatis Plus 配置

```yaml
mybatis-plus:
  mapper-locations: classpath*:/mapper/**/*.xml
  type-aliases-package: com.example.project.entity
  configuration:
    map-underscore-to-camel-case: true
```

---

## 📜 License

MIT
