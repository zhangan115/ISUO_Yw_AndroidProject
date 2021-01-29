package com.sito.library.utils;

import java.math.BigDecimal;

public class NumberUtils {
    public static double formatDouble(Double f) {
        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
