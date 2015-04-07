package io.sinq.codegen.stream;

import java.util.ArrayList;
import java.util.List;

public class StreamData {
    private String pkg;

    private List<TS> ts = new ArrayList<>();

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public List<TS> getTs() {
        return ts;
    }
}
