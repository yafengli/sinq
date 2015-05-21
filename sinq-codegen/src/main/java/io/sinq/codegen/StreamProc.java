package io.sinq.codegen;

import io.sinq.codegen.stream.MC;
import io.sinq.codegen.stream.StreamData;
import io.sinq.codegen.stream.TS;

import java.io.File;
import java.util.HashMap;

import static io.sinq.codegen.util.FileWriter.withWriter;
import static io.sinq.codegen.util.FreeMarkerUtil.baseDir;
import static io.sinq.codegen.util.FreeMarkerUtil.template;

public class StreamProc {

    public static void proc() {
        try {
            HashMap<String, StreamData> map = new HashMap<>();
            map.put("data", store(22));

            File f = new File(baseDir(), "io/sinq/SinqStream.scala");
            withWriter(f, w -> {
                try {
                    template("stream").process(map, w);
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
