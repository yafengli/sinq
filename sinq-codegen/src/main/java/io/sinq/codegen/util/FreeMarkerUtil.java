package io.sinq.codegen.util;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.File;

public class FreeMarkerUtil {

    private static Configuration cfg;

    static {
        try {
            cfg = new Configuration(Configuration.VERSION_2_3_22);
            cfg.setClassForTemplateLoading(FreeMarkerUtil.class, "/META-INF/ftl");
            cfg.setDefaultEncoding("UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Template template(String prefix) throws Exception {
        return cfg.getTemplate(prefix + ".ftl");
    }

    public static File baseDir() {
        String parent = System.getProperty("user.dir");
        try {
            File classDir = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI());
            if (classDir != null)
                parent = classDir.getParentFile().getParent();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File(parent, "generate-source");
    }
}
