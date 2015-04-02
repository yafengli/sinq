package io.sinq.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.sinq.codegen.table.TableData;
import io.sinq.codegen.table.TableField;

import javax.persistence.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TableProc {
    public static void loop(String scanPkg, String outPkg) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassForTemplateLoading(FtlProc.class, "/META-INF/ftl");
        cfg.setDefaultEncoding("UTF-8");
        try {
            Template select = cfg.getTemplate("code.ftl");
            URL url = Thread.currentThread().getContextClassLoader().getResource(scanPkg.replace(".", "/"));
            File file = new File(new URI(url.toString()));
            String[] names = file.list((f, name) -> name.endsWith(".class"));
            for (int i = 0; i < names.length; i++) {
                try {
                    Class c = Class.forName(scanPkg + "." + names[i].replace(".class", ""));
                    proc(select, c, outPkg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void proc(Template select, Class<?> c, String outPkg) {
        try {
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
                            procJoinTable(select, c, joinTable, outPkg);
                        }
                    } else if (field.isAnnotationPresent(Column.class)) {
                        Column column = field.getAnnotation(Column.class);
                        if (column.name() != null) {
                            fd.setColumnId(column.name());
                        } else fd.setColumnId(fd.getName());
                        fd.setTypename(field.getType().getCanonicalName());
                        data.getFields().add(fd);
                    } else {
                        fd.setColumnId(field.getName());
                        fd.setTypename(field.getType().getCanonicalName());
                        data.getFields().add(fd);
                    }
                });
                Map<String, TableData> map = new HashMap<>();
                map.put("data", data);
                withWriter(new File(outPkg.replace(".", "/") + "/" + data.getName() + ".scala"), w -> {
                    try {
                        select.process(map, w);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void procJoinTable(Template select, Class<?> c, JoinTable joinTable, String outPkg) {
        try {
            String tableName = joinTable.name();
            Map<String, TableData> map = new HashMap<>();
            TableData data = new TableData();
            data.setName(tableName.toUpperCase());
            data.setClassname(c.getSimpleName().toUpperCase());
            data.setPkg(outPkg);
            data.setTablename(joinTable.name());
            Arrays.asList(joinTable.joinColumns()).stream().forEach(jc -> {
                TableField fd = new TableField();
                fd.setColumnId(jc.name());
                fd.setName(jc.name());
                //TODO
                fd.setTypename("java.lang.Long");
                data.getFields().add(fd);
            });

            Arrays.asList(joinTable.inverseJoinColumns()).stream().forEach(jc -> {
                TableField fd = new TableField();
                fd.setColumnId(jc.name());
                fd.setName(jc.name());
                //TODO
                fd.setTypename("java.lang.Long");
                data.getFields().add(fd);
            });
            map.put("data", data);

            File f = new File(outPkg.replace(".", "/") + "/" + data.getClassname() + ".scala");
            System.out.println(f.getAbsolutePath());
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
        return Arrays.asList(c.getDeclaredFields()).stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().get().getType().getName();
    }

    private static void withWriter(File f, Consumer<BufferedWriter> call) {
        if (!f.getParentFile().exists()) {
            try {
                f.getParentFile().mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(f, false))) {
            call.accept(writer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
