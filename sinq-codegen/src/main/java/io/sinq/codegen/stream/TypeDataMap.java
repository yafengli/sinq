package io.sinq.codegen.stream;

import java.util.HashMap;
import java.util.Map;

public class TypeDataMap {
    public final static Map<String, String> HIBERNATE = new HashMap<>();

    static {
        HIBERNATE.put("int", "Int");
        HIBERNATE.put("java.lang.Integer", "Int");
        HIBERNATE.put("long", "java.math.BigInteger");
        HIBERNATE.put("java.lang.Long", "java.math.BigInteger");
        HIBERNATE.put("boolean", "Boolean");
        HIBERNATE.put("java.lang.Boolean", "Boolean");
        HIBERNATE.put("java.lang.String", "String");
    }
}
