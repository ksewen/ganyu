package com.github.ksewen.ganyu.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ksewen
 * @date 31.05.2023 18:38
 */
public class BusinessHelpers {

    private final String SPLIT = ",";

    public String listToStringCommaSeparated(List<String> list) {
        return String.join(this.SPLIT, list);
    }

    public List<String> stringCommaSeparatedToList(String str) {
        String[] split = str.split(this.SPLIT);
        List<String> result = new ArrayList<>();
        for (String s : split) {
            result.add(s);
        }
        return result;
    }
}
