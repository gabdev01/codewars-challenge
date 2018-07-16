package codewars.tuigroup.com.codewars.ui.util;

import java.util.HashMap;
import java.util.Map;

public enum RequestResultType {

    SUCCESS_RESULT(1),

    SUCCESS_EMPTY(2),

    ERROR(3),

    UNDEFINED(-1);

    private final int value;

    RequestResultType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static Map<Integer, RequestResultType> VALUES_MAP = null;

    private static void initializeMap() {
        VALUES_MAP = new HashMap<>();
        for (RequestResultType value : RequestResultType.values()) {
            VALUES_MAP.put(value.getValue(), value);
        }
    }

    public static RequestResultType from(int value) {
        if (VALUES_MAP == null) {
            initializeMap();
        }
        return VALUES_MAP.get(value);
    }
}
