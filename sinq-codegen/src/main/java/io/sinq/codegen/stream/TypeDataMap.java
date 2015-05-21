package io.sinq.codegen.stream;

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

    public static Map<String, String> factory() {
        return TypeMap;
    }

    public static Map<String, String> add(String fromTypeName, String toTypeName) {
        TypeMap.put(fromTypeName, toTypeName);
        return TypeMap;
    }

    public static Map<String, String> addAll(Map<String, String> map) {
        TypeMap.putAll(map);
        return TypeMap;
    }
}
