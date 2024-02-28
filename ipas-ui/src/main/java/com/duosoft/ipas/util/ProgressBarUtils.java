package com.duosoft.ipas.util;

public class ProgressBarUtils {

    public static int calculateProgress(int totalElements, int index, boolean isIndexStartFromZero) {
        double indexSum = index + (isIndexStartFromZero ? 1 : 0);
        return (int) (100 / (totalElements / indexSum));
    }

}
