package io.sinq.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.sinq.codegen.stream.MC;
import io.sinq.codegen.stream.StreamData;
import io.sinq.codegen.stream.TS;
import io.sinq.codegen.table.TableData;
import io.sinq.codegen.table.TableField;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TableProc {
    public static void main(String[] args) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassForTemplateLoading(FtlProc.class, "/META-INF/ftl");
        cfg.setDefaultEncoding("UTF-8");

        try {
            Template select = cfg.getTemplate("code.ftl");
            StringWriter writer = new StringWriter();
            Map<String, TableData> map = new HashMap<>();
            map.put("data", store(22));
            System.out.println("map:" + map);
            select.process(map, writer);
            System.out.println("###############");
            System.out.println(writer.toString());
            System.out.println("###############");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TableData store(int count) {
        TableData data = new TableData();
        data.setPkg("io.sinq");
        data.setName("USER");
        data.setClasspath("models.User");
        data.setClassname("User");
        data.setTablename("t_user");
        for (int i = 0; i < 5; i++) {
            TableField f = new TableField();
            f.setColumnId("s_" + i);
            f.setName("n_" + i);
            f.setTypename("Long");
            data.getFields().add(f);
        }
        return data;
    }
}
