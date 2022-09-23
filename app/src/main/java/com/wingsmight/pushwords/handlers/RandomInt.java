package com.wingsmight.pushwords.handlers;

import java.util.concurrent.ThreadLocalRandom;

public class RandomInt {
    public static int get(int minIncluded, int maxExcluded) {
        return ThreadLocalRandom.current().nextInt(minIncluded, maxExcluded);
    }
}
