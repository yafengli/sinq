package io.sinq.codegen.table;

import java.util.ArrayList;
import java.util.List;

public class TableData {
    private String pkg;
    private String name;
    private String entityClassName;
    private String tableName;

    private List<TableField> fields = new ArrayList<>();

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntityClassName() {
        return entityClassName;
    }

    public void setEntityClassName(String entityClassName) {
        this.entityClassName = entityClassName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<TableField> getFields() {
        return fields;
    }
}
