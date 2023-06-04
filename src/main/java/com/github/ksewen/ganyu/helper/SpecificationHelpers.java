package com.github.ksewen.ganyu.helper;

/**
 * @author ksewen
 * @date 01.06.2023 22:29
 */
public class SpecificationHelpers {

    public String generateLeftFuzzyKeyword(String str) {
        StringBuilder builder = new StringBuilder("%").append(str);
        return builder.toString();
    }

    public String generateRightFuzzyKeyword(String str) {
        StringBuilder builder = new StringBuilder(str).append("%");
        return builder.toString();
    }

    public String generateFullFuzzyKeyword(String str) {
        StringBuilder builder = new StringBuilder("%").append(str).append("%");
        return builder.toString();
    }
}
