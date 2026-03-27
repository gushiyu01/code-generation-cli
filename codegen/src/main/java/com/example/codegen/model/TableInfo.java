package com.example.codegen.model;

import com.example.codegen.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 表信息
 *
 * @author CodeGenerator
 */
public class TableInfo {

    private String tableName;
    private String entityName;
    private String comment;
    private List<ColumnInfo> columns = new ArrayList<>();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
        // 自动从表名推导实体名（可覆盖）
        this.entityName = StringUtil.toPascalCase(tableName);
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public List<ColumnInfo> getColumns() {
        return columns;
    }

    public void setColumns(List<ColumnInfo> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "TableInfo{" +
                "tableName='" + tableName + '\'' +
                ", entityName='" + entityName + '\'' +
                ", comment='" + comment + '\'' +
                ", columns=" + columns +
                '}';
    }
}
