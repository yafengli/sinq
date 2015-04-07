package io.sinq.codegen.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;

public class FreeMarkerUtil {

    private static Configuration cfg;
    private static File baseDir;

    static {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "/META-INF/ftl");
            cfg.setDefaultEncoding("UTF-8");

            baseDir = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Template template(String prefix) throws Exception {
        return cfg.getTemplate(prefix + ".ftl");
    }

    public static File baseDir() {
        return baseDir;
    }
}
