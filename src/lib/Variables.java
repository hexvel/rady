package lib;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    private static final Map<String, Double> variables;

    static {
        variables = new HashMap<>();
        variables.put("PI", Math.PI);
        variables.put("E", Math.E);
    }

    public static boolean isExists(String key) {
        return variables.containsKey(key);
    }

    public static double getByKey(String key) {
        if (!isExists(key)) return 0;
        return variables.get(key);
    }

    public static void set(String key, double value) {
        variables.put(key, value);
    }
}
