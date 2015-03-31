package io.sinq.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.sinq.codegen.table.TableData;
import io.sinq.codegen.table.TableField;

import javax.persistence.*;
import java.io.File;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class TableProc {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage:run [scan.entity.package] [/out/dir]");

        } else {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setClassForTemplateLoading(FtlProc.class, "/META-INF/ftl");
            cfg.setDefaultEncoding("UTF-8");

            try {
                Template select = cfg.getTemplate("code.ftl");
                loop(select, args[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void loop(Template select, String pkg) {
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource(pkg.replace(".", "/"));
            File file = new File(new URI(url.toString()));
            String[] names = file.list((f, name) -> name.endsWith(".class"));
            for (int i = 0; i < names.length; i++) {
                try {
                    Class c = Class.forName(pkg + "." + names[i].replace(".class", ""));
                    proc(select, c, pkg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    private static void proc(Template select, Class<?> c, String pkg) {
        try {
            if (c.isAnnotationPresent(Table.class)) {
                StringWriter writer = new StringWriter();
                Table annotation = c.getAnnotation(Table.class);
                TableData data = new TableData();
                data.setPkg(pkg);
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
                            procJoinTable(select, c, joinTable, pkg);
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
                select.process(map, writer);
                System.out.println(writer.toString());
                System.out.println("----------------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void procJoinTable(Template select, Class<?> c, JoinTable joinTable, String pkg) {
        try {
            String tableName = joinTable.name();
            StringWriter writer = new StringWriter();
            Map<String, TableData> map = new HashMap<>();
            TableData data = new TableData();
            data.setName(tableName.toUpperCase());
            data.setClassname(c.getName());
            data.setPkg(pkg);
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
            select.process(map, writer);
            System.out.println(writer.toString());
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String findPkType(Class<?> c) {
        return Arrays.asList(c.getDeclaredFields()).stream().filter(f -> f.isAnnotationPresent(Id.class)).findFirst().get().getType().getName();
    }
}
