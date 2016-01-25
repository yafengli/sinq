package io.sinq.codegen;

import freemarker.template.Template;
import io.sinq.codegen.table.TableData;
import io.sinq.codegen.table.TableField;

import javax.persistence.*;
import java.io.File;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.sinq.codegen.util.FileWriter.withWriter;
import static io.sinq.codegen.util.FreeMarkerUtil.baseDir;
import static io.sinq.codegen.util.FreeMarkerUtil.template;

public class TableProc {

    private String outPkg;
    private String scanPkg;
    private String suffix = "T_";
    private Map<String, String> typeDataMap;

    public TableProc(String scanPkg, String outPkg, Map<String, String> typeDataMap) {
        this.scanPkg = scanPkg;
        this.outPkg = outPkg;
        this.typeDataMap = typeDataMap;
    }

    public TableProc(String scanPkg, String outPkg, String suffix, Map<String, String> typeDataMap) {
        this.scanPkg = scanPkg;
        this.outPkg = outPkg;
        this.suffix = suffix;
        this.typeDataMap = typeDataMap;
    }


    public void proc() {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(scanPkg.replace(".", "/"));
            System.out.println("url:" + url);
            File file = new File(new URI(url.toString()));
            String[] names = file.list((f, name) -> name.endsWith(".class"));
            for (int i = 0; i < names.length; i++) {
                try {
                    Class c = Class.forName(scanPkg + "." + names[i].replace(".class", ""));
                    loop(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void loop(Class<?> c) {
        try {
            Template tpl = template("code");
            if (c.isAnnotationPresent(Table.class)) {
                Table annotation = c.getAnnotation(Table.class);
                TableData data = new TableData();
                data.setPkg(outPkg);
                data.setName(suffix + c.getSimpleName().toUpperCase());
                data.setEntityClassName(c.getName());
                data.setTableName(annotation.name());

                Arrays.asList(c.getDeclaredFields()).stream().forEach(field -> {
                    TableField fd = new TableField();
                    fd.setName(field.getName());

                    if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if (column.name() != null) {
                            fd.setColumnId(column.name());
                        } else fd.setColumnId(fd.getName());
                        fd.setTypename(typeMap(field.getType().getName()));
                        data.getFields().add(fd);
                    } else if (field.isAnnotationPresent(JoinColumn.class)) {
                        JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                        fd.setTypename(findPkTypeName(field.getType()));
                        fd.setColumnId(joinColumn.name());
                        data.getFields().add(fd);
                    } else if (field.isAnnotationPresent(OneToOne.class)) {
                        mappedBy(field.getAnnotation(OneToOne.class).mappedBy(), field, fd, data);
                    } else if (field.isAnnotationPresent(ManyToOne.class)) {
                        fd.setName(field.getName() + "_" + findPk(field.getType()).getName());
                        fd.setTypename(findPkTypeName(field.getType()));
                        fd.setColumnId(fd.getName());
                        data.getFields().add(fd);
                    } else if (field.isAnnotationPresent(OneToMany.class)) {
                        //SKIP
                    } else if (field.isAnnotationPresent(ManyToMany.class)) {
                        if (field.isAnnotationPresent(JoinTable.class)) {
                            JoinTable joinTable = field.getAnnotation(JoinTable.class);
                            procJoinTable(tpl, c, joinTable, outPkg, baseDir());
                        }
                    } else {
                        fd.setColumnId(field.getName());
                        fd.setTypename(typeMap(field.getType().getName()));
                        data.getFields().add(fd);
                    }
                });
                Map<String, TableData> map = new HashMap<>();
                map.put("data", data);
                File f = new File(baseDir(), outPkg.replace(".", "/") + "/" + data.getName() + ".scala");
                withWriter(f, w -> {
                    try {
                        tpl.process(map, w);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mappedBy(String mappedBy, Field field, TableField fd, TableData data) {
        if (mappedBy == null || mappedBy.trim().length() <= 0) {
            fd.setTypename(findPkTypeName(field.getType()));
            fd.setColumnId(field.getName());
            data.getFields().add(fd);
        }
    }

    private void procJoinTable(Template select, Class<?> c, JoinTable joinTable, String outPkg, File baseDir) {
        try {
            String tableName = joinTable.name();
            Map<String, TableData> map = new HashMap<>();
            TableData data = new TableData();
            data.setName("_" + tableName.toUpperCase());
            data.setEntityClassName(c.getName());
            data.setPkg(outPkg);
            data.setTableName(joinTable.name());

            Consumer<JoinColumn> consumer = (JoinColumn t) -> {
                TableField fd = new TableField();
                fd.setColumnId(t.name());
                fd.setName(t.name());
                //TODO own id type
                if (t.referencedColumnName() != null) {

                } else {
                }
                fd.setTypename("Long");
                data.getFields().add(fd);
            };

            Arrays.asList(joinTable.joinColumns()).stream().forEach(consumer);
            Arrays.asList(joinTable.inverseJoinColumns()).stream().forEach(consumer);

            map.put("data", data);

            File f = new File(baseDir, outPkg.replace(".", "/") + "/" + data.getName() + ".scala");
            withWriter(f, w -> {
                try {
                    select.process(map, w);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String findPkTypeName(Class<?> c) {
        return typeMap(findPk(c).getType().getName());
    }

    private static Field findPk(Class<?> c) {
        return Arrays.asList(c.getDeclaredFields()).stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().get();
    }

    private String typeMap(String typeName) {
        if (typeDataMap.containsKey(typeName)) return typeDataMap.get(typeName);
        else return typeName;
    }
}