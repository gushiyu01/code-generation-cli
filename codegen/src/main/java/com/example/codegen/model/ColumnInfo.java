package com.example.codegen.model;

/**
 * 列信息
 *
 * @author CodeGenerator
 */
public class ColumnInfo {

    private String columnName;
    private String dataType;
    private String columnComment;
    private boolean primaryKey;
    private boolean autoIncrement;
    private boolean nullable;
    private int columnSize;
    private int decimalDigits;
    private String javaType;
    private String javaProperty;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public boolean isPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public void setColumnSize(int columnSize) {
        this.columnSize = columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public void setDecimalDigits(int decimalDigits) {
        this.decimalDigits = decimalDigits;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getJavaProperty() {
        return javaProperty;
    }

    public void setJavaProperty(String javaProperty) {
        this.javaProperty = javaProperty;
    }

    /**
     * 根据JDBC类型推断Java类型
     * 注意：MySQL BIT(1) 实际是布尔类型，BIT(N>1) 是 bitset，需根据 columnSize 区分
     */
    public static String getJavaTypeByJdbcType(int jdbcType, String typeName, int columnSize) {
        // MySQL BIT 类型：BIT(1) → Boolean，BIT(N>1) → byte[]
        if (typeName.equalsIgnoreCase("BIT") && columnSize == 1) {
            return "Boolean";
        }
        if (typeName.contains("UNSIGNED")) {
            if (jdbcType == java.sql.Types.INTEGER) return "Long";
            if (jdbcType == java.sql.Types.TINYINT) return "Short";
            if (jdbcType == java.sql.Types.BIGINT) return "BigInteger";
        }
        switch (jdbcType) {
            case java.sql.Types.BIT:
                // 非 MySQL 或 MySQL BIT(N>1)
                return columnSize == 1 ? "Boolean" : "byte[]";
            case java.sql.Types.TINYINT:
                return "Integer";
            case java.sql.Types.SMALLINT:
                return "Short";
            case java.sql.Types.INTEGER:
                return "Integer";
            case java.sql.Types.BIGINT:
                return "Long";
            case java.sql.Types.FLOAT:
            case java.sql.Types.REAL:
                return "Float";
            case java.sql.Types.DOUBLE:
            case java.sql.Types.NUMERIC:
            case java.sql.Types.DECIMAL:
                return "Double";
            case java.sql.Types.CHAR:
            case java.sql.Types.VARCHAR:
            case java.sql.Types.LONGVARCHAR:
            case java.sql.Types.NCHAR:
            case java.sql.Types.NVARCHAR:
            case java.sql.Types.LONGNVARCHAR:
            case java.sql.Types.CLOB:
            case java.sql.Types.NCLOB:
                return "String";
            case java.sql.Types.DATE:
            case java.sql.Types.TIME:
            case java.sql.Types.TIMESTAMP:
            case java.sql.Types.TIME_WITH_TIMEZONE:
            case java.sql.Types.TIMESTAMP_WITH_TIMEZONE:
                return "LocalDateTime";
            case java.sql.Types.BINARY:
            case java.sql.Types.VARBINARY:
            case java.sql.Types.LONGVARBINARY:
            case java.sql.Types.BLOB:
                return "byte[]";
            case java.sql.Types.BOOLEAN:
                return "Boolean";
            default:
                return "String";
        }
    }

    @Override
    public String toString() {
        return "ColumnInfo{" +
                "columnName='" + columnName + '\'' +
                ", dataType='" + dataType + '\'' +
                ", primaryKey=" + primaryKey +
                ", javaType='" + javaType + '\'' +
                '}';
    }
}
