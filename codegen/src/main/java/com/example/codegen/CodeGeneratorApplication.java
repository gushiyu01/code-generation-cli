package com.example.codegen;

import com.example.codegen.config.CodeGeneratorConfig;
import com.example.codegen.generator.CodeGenerator;
import com.example.codegen.util.DbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.util.List;

/**
 * 代码生成器启动类
 *
 * 支持三种模式：
 * 1. 单表生成：setTableName("sys_user")
 * 2. 批量生成（按前缀）：setTablePrefix("sys_")  -> 生成所有 sys_ 开头的表
 * 3. 批量生成（指定表）：setTables("sys_user,sys_role,sys_menu")
 *
 * @author CodeGenerator
 */
public class CodeGeneratorApplication {

    private static final Logger log = LoggerFactory.getLogger(CodeGeneratorApplication.class);

    public static void main(String[] args) {
        // ========== 配置区 ==========
        CodeGeneratorConfig config = new CodeGeneratorConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/your_database?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
        config.setUsername("root");
        config.setPassword("your_password");
        config.setBasePackage("com.example.project");
        config.setOutputPath("your_output_path");

        // ========== 选择生成模式 ==========

        // 【模式一】单表生成
        // config.setTableName("sys_user");

        // 【模式二】批量生成（表名前缀）
        config.setTablePrefix("sys_");

        // 【模式三】指定多张表（逗号分隔）
        // config.setTables("sys_user,sys_role,sys_menu");

        // ========== 执行生成 ==========
        CodeGenerator generator = new CodeGenerator(config);
        generator.generate();
    }

    /**
     * 列出数据库中所有表（调试用）
     */
    public static void listAllTables(CodeGeneratorConfig config) {
        Connection connection = null;
        try {
            connection = DbUtil.getConnection(config);
            List<String> tables = DbUtil.getTables(connection);
            log.info("数据库中共 {} 张表:", tables.size());
            for (int i = 0; i < tables.size(); i++) {
                log.info("  {}. {}", i + 1, tables.get(i));
            }
        } catch (Exception e) {
            log.error("获取表列表失败", e);
        } finally {
            DbUtil.closeConnection(connection);
        }
    }
}
