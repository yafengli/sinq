package io.sinq.codegen;

import io.sinq.codegen.stream.TypeDataMap;

import java.io.File;

public class GenerationBoot {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage Option: [-t|-s] [out.dir]");
        } else {
            String option = args[0];
            File outDir = new File(args[1]);
            if (option.equalsIgnoreCase("-s")) {
                StreamProc.proc(outDir);
            } else if (option.equalsIgnoreCase("-t")) {
                TableProc tableProc = new TableProc("models", "gen", outDir, TypeDataMap.HIBERNATE);
                tableProc.procByClassLoader();
            } else {
                System.err.println("Usage Option: [-t|-s] [out.dir]");
            }
        }
    }
}
