package io.sinq.codegen;

import freemarker.template.Configuration;
import freemarker.template.Template;
import io.sinq.codegen.stream.MC;
import io.sinq.codegen.stream.StreamData;
import io.sinq.codegen.stream.TS;

import java.io.File;
import java.util.HashMap;

import static io.sinq.codegen.util.FileWriter.withWriter;

public class StreamProc {
    public static void proc() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        cfg.setClassForTemplateLoading(StreamProc.class, "/ftl");
        cfg.setDefaultEncoding("UTF-8");

        try {
            Template select = cfg.getTemplate("stream.ftl");
            HashMap<String, StreamData> map = new HashMap<>();
            map.put("data", store(22));

            File baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            File f = new File(baseDir, "io/sinq/SinqStream.scala");
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
