package io.sinq.codegen.util;

import java.io.BufferedWriter;
import java.io.File;
import java.util.function.Consumer;

public class FileWriter {
    public static void withWriter(File f, Consumer<BufferedWriter> call) {
        if (!f.getParentFile().exists()) {
            try {
                f.getParentFile().mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(f, false))) {
            call.accept(writer);
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
