package io.sinq.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.sinq.codegen.stream.MC;
import io.sinq.codegen.stream.StreamData;
import io.sinq.codegen.stream.TS;

import java.io.StringWriter;
import java.util.HashMap;

public class FtlProc {
    public static void main(String[] args) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassForTemplateLoading(FtlProc.class, "/META-INF/ftl");
        cfg.setDefaultEncoding("UTF-8");

        try {
            Template select = cfg.getTemplate("select.ftl");
            StringWriter writer = new StringWriter();
            HashMap<String, StreamData> map = new HashMap<>();
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

    private static StreamData store(int count) {
        StreamData data = new StreamData();
        data.setPkg("io.sinq");

        for (int i = 1; i <= count; i++) {
            TS s = new TS();
            for (int j = 1; j <= i; j++) {
                MC c = new MC();
                c.setName("c" + j);
                c.setTpe("T" + j);
                s.getCs().add(c);
            }
            data.getTs().add(s);
        }
        return data;
    }
}
