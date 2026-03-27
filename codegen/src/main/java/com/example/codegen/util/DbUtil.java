package com.example.codegen.util;

import com.example.codegen.config.CodeGeneratorConfig;
import com.example.codegen.model.ColumnInfo;
import com.example.codegen.model.TableInfo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据库工具类
 *
 * @author CodeGenerator
 */
public class DbUtil {

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(CodeGeneratorConfig config) throws SQLException {
        return DriverManager.getConnection(
                config.getJdbcUrl(),
                config.getUsername(),
                config.getPassword()
        );
    }

    /**
     * 关闭连接
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                // ignore
            }
        }
    }

    /**
     * 获取表信息
     */
    public static TableInfo getTableInfo(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);

        // 获取表注释
        try (ResultSet rs = metaData.getTables(null, null, tableName, new String[]{"TABLE"})) {
            if (rs.next()) {
                tableInfo.setComment(rs.getString("REMARKS"));
            }
        }

        return tableInfo;
    }

    /**
     * 获取数据库所有表
     */
    public static List<String> getTables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        try (ResultSet rs = metaData.getTables(null, null, "%", new String[]{"TABLE"})) {
            while (rs.next()) {
                tables.add(rs.getString("TABLE_NAME"));
            }
        }
        return tables;
    }

    /**
     * 获取列信息
     */
    public static List<ColumnInfo> getColumns(Connection connection, String tableName) throws SQLException {
        DatabaseMetaData metaData = connection.getMetaData();
        List<ColumnInfo> columns = new ArrayList<>();

        // 获取主键列
        List<String> primaryKeys = new ArrayList<>();
        try (ResultSet rs = metaData.getPrimaryKeys(null, null, tableName)) {
            while (rs.next()) {
                primaryKeys.add(rs.getString("COLUMN_NAME"));
            }
        }

        // 获取所有列
        try (ResultSet rs = metaData.getColumns(null, null, tableName, null)) {
            while (rs.next()) {
                ColumnInfo column = new ColumnInfo();
                String columnName = rs.getString("COLUMN_NAME");
                String dataTypeName = rs.getString("TYPE_NAME");
                int dataType = rs.getInt("DATA_TYPE");
                boolean isNullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
                int columnSize = rs.getInt("COLUMN_SIZE");
                int decimalDigits = rs.getInt("DECIMAL_DIGITS");
                String remarks = rs.getString("REMARKS");
                String isAutoIncrement = rs.getString("IS_AUTOINCREMENT");

                column.setColumnName(columnName);
                column.setDataType(dataTypeName);
                column.setColumnSize(columnSize);
                column.setDecimalDigits(decimalDigits);
                column.setNullable(isNullable);
                column.setColumnComment(remarks != null ? remarks : "");
                column.setAutoIncrement("YES".equalsIgnoreCase(isAutoIncrement));
                column.setPrimaryKey(primaryKeys.contains(columnName));

                // 设置Java类型
                String javaType = ColumnInfo.getJavaTypeByJdbcType(dataType, dataTypeName, columnSize);
                column.setJavaType(javaType);
                column.setJavaProperty(StringUtil.toCamelCase(columnName));

                columns.add(column);
            }
        }

        return columns;
    }
}
