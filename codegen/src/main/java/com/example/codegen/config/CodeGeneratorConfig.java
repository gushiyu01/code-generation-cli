package com.example.codegen.config;

/**
 * 代码生成器配置
 *
 * @author CodeGenerator
 */
public class CodeGeneratorConfig {

    private String jdbcUrl;
    private String username;
    private String password;
    private String basePackage = "com.example.project";
    private String outputPath = ".";

    // 单表生成
    private String tableName;

    // 批量生成：表名前缀，如 "sys_" 表示生成所有 sys_ 开头的表
    private String tablePrefix;

    // 指定多表生成（逗号分隔），优先级高于 tableName 和 tablePrefix
    private String tables;

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String getTables() {
        return tables;
    }

    public void setTables(String tables) {
        this.tables = tables;
    }

    /**
     * 获取要生成的所有表名
     */
    public java.util.List<String> resolveTableNames(java.util.List<String> allTables) {
        java.util.List<String> result = new java.util.ArrayList<>();

        // 1. 先处理指定的 tables（优先级最高）
        if (tables != null && !tables.isBlank()) {
            for (String t : tables.split(",")) {
                String trimmed = t.trim();
                if (!trimmed.isEmpty()) {
                    result.add(trimmed);
                }
            }
            return result;
        }

        // 2. 其次处理单表
        if (tableName != null && !tableName.isBlank()) {
            result.add(tableName.trim());
            return result;
        }

        // 3. 最后处理前缀批量
        if (tablePrefix != null && !tablePrefix.isBlank()) {
            return allTables.stream()
                    .filter(t -> t.toLowerCase().startsWith(tablePrefix.toLowerCase()))
                    .toList();
        }

        return result;
    }
}
