package io.sinq.codegen.table;

import java.util.ArrayList;
import java.util.List;

public class TableData {
    private String pkg;
    private String name;
    private String classname;
    private String classpath;
    private String tablename;

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

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getClasspath() {
        return classpath;
    }

    public void setClasspath(String classpath) {
        this.classpath = classpath;
    }

    public String getTablename() {
        return tablename;
    }

    public void setTablename(String tablename) {
        this.tablename = tablename;
    }

    public List<TableField> getFields() {
        return fields;
    }
}
