package codewars.tuigroup.com.codewars.ui.challenges;

import java.util.HashMap;
import java.util.Map;

public enum ChallengesViewType {

    COMPLETED(1),

    AUTHORED(2);

    private final int value;

    ChallengesViewType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    private static Map<Integer, ChallengesViewType> VALUES_MAP = null;

    private static void initializeMap() {
        VALUES_MAP = new HashMap<>();
        for (ChallengesViewType value : ChallengesViewType.values()) {
            VALUES_MAP.put(value.getValue(), value);
        }
    }

    public static ChallengesViewType from(int value) {
        if (VALUES_MAP == null) {
            initializeMap();
        }
        return VALUES_MAP.get(value);
    }
}