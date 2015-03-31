package io.sinq.codegen.stream;

import java.util.ArrayList;
import java.util.List;

public class TS {
    private List<MC> cs = new ArrayList<>();

    private String tpe;
    private String cols;

    public String getTpe() {
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (MC c : getCs()) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(c.getTpe());
        }
        return result.toString();
    }

    public String getCols() {
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (MC c : getCs()) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(c.getName() + ":Column[" + c.getTpe() + "]");
        }
        return result.toString();
    }

    public List<MC> getCs() {
        return cs;
    }
}
