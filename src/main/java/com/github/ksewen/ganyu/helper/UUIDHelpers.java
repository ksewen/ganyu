package com.github.ksewen.ganyu.helper;

import org.springframework.util.StringUtils;

import java.util.UUID;

/**
 * @author ksewen
 * @date 12.05.2023 22:21
 */
public class UUIDHelpers {

    public String getShortUUID(String uuid) {
        if (!isValidUUID(uuid)) {
            return UUID.randomUUID().toString().replace("-", "");
        }
        return uuid.replace("-", "");
    }

    public String getShortUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String getRandomUUID() {
        return getRandomUUID(null);
    }

    public String getRandomUUID(String str) {
        if (!StringUtils.hasLength(str)) {
            return UUID.randomUUID().toString();
        } else {
            return UUID.nameUUIDFromBytes(str.getBytes()).toString();
        }
    }

    public static boolean isValidUUID(String uuid) {
        if (!StringUtils.hasLength(uuid)) {
            return false;
        }
        final String longPattern = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";
        final String shortPattern = "^[0-9a-f]{8}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{4}[0-9a-f]{12}$";
        return uuid.matches(longPattern) || uuid.matches(shortPattern);
    }
}
