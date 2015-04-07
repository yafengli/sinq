package io.sinq.codegen;

import freemarker.template.Template;
import io.sinq.codegen.table.TableData;
import io.sinq.codegen.table.TableField;

import javax.persistence.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.sinq.codegen.TypeDataMap.getTypeMap;
import static io.sinq.codegen.util.FileWriter.withWriter;
import static io.sinq.codegen.util.FreeMarkerUtil.baseDir;
import static io.sinq.codegen.util.FreeMarkerUtil.template;

public class TableProc {
    public static void loop(String scanPkg, String outPkg) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(scanPkg.replace(".", "/"));
            File file = new File(new URI(url.toString()));
            String[] names = file.list((f, name) -> name.endsWith(".class"));
            for (int i = 0; i < names.length; i++) {
                try {
                    Class c = Class.forName(scanPkg + "." + names[i].replace(".class", ""));
                    proc(c, outPkg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void proc(Class<?> c, String outPkg) {
        try {
            Template tpl = template("code");
            if (c.isAnnotationPresent(Table.class)) {
                Table annotation = c.getAnnotation(Table.class);
                TableData data = new TableData();
                data.setPkg(outPkg);
                data.setName(c.getSimpleName().toUpperCase());
                data.setClassname(c.getName());
                data.setTablename(annotation.name());

                Arrays.asList(c.getDeclaredFields()).stream().forEach(field -> {
                    TableField fd = new TableField();
                    fd.setName(field.getName());
                    if (field.isAnnotationPresent(OneToOne.class) || field.isAnnotationPresent(ManyToOne.class) || field.isAnnotationPresent(OneToMany.class)) {
                        if (field.isAnnotationPresent(JoinColumn.class)) {
                            JoinColumn joinColumn = field.getAnnotation(JoinColumn.class);
                            fd.setTypename(findPkType(field.getType()));
                            fd.setColumnId(joinColumn.name());
                            data.getFields().add(fd);
                        }
                    } else if (field.isAnnotationPresent(ManyToMany.class)) {
                        if (field.isAnnotationPresent(JoinTable.class)) {
                            JoinTable joinTable = field.getAnnotation(JoinTable.class);
                            procJoinTable(tpl, c, joinTable, outPkg, baseDir());
                        }
                    } else if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if (column.name() != null) {
                            fd.setColumnId(column.name());
                        } else fd.setColumnId(fd.getName());
                        fd.setTypename(findType(field.getType().getName()));
                        data.getFields().add(fd);
                    } else {
                        fd.setColumnId(field.getName());
                        fd.setTypename(findType(field.getType().getName()));
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

    private static void procJoinTable(Template select, Class<?> c, JoinTable joinTable, String outPkg, File baseDir) {
        try {
            String tableName = joinTable.name();
            Map<String, TableData> map = new HashMap<>();
            TableData data = new TableData();
            data.setName(tableName.toUpperCase());
            data.setClassname(c.getName());
            data.setPkg(outPkg);
            data.setTablename(joinTable.name());
            Arrays.asList(joinTable.joinColumns()).stream().forEach(jc -> {
                TableField fd = new TableField();
                fd.setColumnId(jc.name());
                fd.setName(jc.name());
                //TODO
                fd.setTypename("Long");
                data.getFields().add(fd);
            });

            Arrays.asList(joinTable.inverseJoinColumns()).stream().forEach(jc -> {
                TableField fd = new TableField();
                fd.setColumnId(jc.name());
                fd.setName(jc.name());
                //TODO
                fd.setTypename("Long");
                data.getFields().add(fd);
            });
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

    private static String findPkType(Class<?> c) {
        String typeName = Arrays.asList(c.getDeclaredFields()).stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().get().getType().getName();

        return findType(typeName);
    }

    private static String findType(String typeName) {
        if (getTypeMap().containsKey(typeName)) return getTypeMap().get(typeName);
        else return typeName;
    }
}