package io.sinq.codegen;

import java.util.HashMap;
import java.util.Map;

public class TypeDataMap {
    private final static Map<String, String> TypeMap = new HashMap<>();

    static {
        TypeMap.put("long", "Long");
        TypeMap.put("int", "Int");
        TypeMap.put("boolean", "Boolean");
        TypeMap.put("java.lang.Long", "Long");
        TypeMap.put("java.lang.Integer", "Int");
        TypeMap.put("java.lang.Boolean", "Boolean");
        TypeMap.put("java.lang.String", "String");
    }

    public static Map<String, String> getTypeMap() {
        return TypeMap;
    }

    public static void map(String fromTypeName, String toTypeName) {
        TypeMap.put(fromTypeName, toTypeName);
    }
}
