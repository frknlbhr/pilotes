package com.tui.proof.dto;

import java.util.HashMap;
import java.util.Map;

public enum PilotesPortionEnum {

    HALF(5),
    ONE(10),
    ONE_AND_HALF(15);

    private final int numberOfMeatBalls;

    PilotesPortionEnum(int numberOfMeatBalls) {
        this.numberOfMeatBalls = numberOfMeatBalls;
    }

    public int getNumberOfMeatBalls() {
        return numberOfMeatBalls;
    }

    private static final Map<Integer, PilotesPortionEnum> map;

    static {
        map = new HashMap<>();
        for (PilotesPortionEnum pilotesPortionEnum : PilotesPortionEnum.values()) {
            map.put(pilotesPortionEnum.numberOfMeatBalls, pilotesPortionEnum);
        }
    }

    public static PilotesPortionEnum findByNumberOfMeatBalls(int numberOfMeatBalls) {
        return map.get(numberOfMeatBalls);
    }
}
