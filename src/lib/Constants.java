package lib;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    private static Map<String, Double> constants;

    static {
        constants = new HashMap<>();
        constants.put("PI", Math.PI);
        constants.put("E", Math.E);
    }

    public static boolean IsExists(String key) {
        return constants.containsKey(key);
    }

    public static double GetByKey(String key) {
        if (!IsExists(key)) return 0;
        return constants.get(key);
    }
}
