package com.dongpv.sns.identity.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {
    public static String getValue(String val) {
        if (val != null) {
            return val;
        } else {
            return "";
        }
    }

    public static String camelToSnake(String str) {
        // Regular Expression
        String regex = "([a-z])([A-Z]+)";

        // Replacement string
        String replacement = "$1_$2";

        // Replace the given regex
        // with replacement string
        // and convert it to lower case.
        str = str.replaceAll(regex, replacement).toLowerCase();

        // return string
        return str;
    }

    public static String getValueByRegex(String value, String _regex) {
        try {
            var map = new HashMap<String, String>();
            var regex = _regex.replace("{", "");
            regex = regex.replace("}", "");
            List<String> list = java.util.Arrays.asList(regex.split("[｜|]"));
            list.forEach(x -> {
                var arrVal = x.split("[：:]");
                map.put(arrVal[0].trim(), arrVal[1]);
            });
            return map.getOrDefault(value, "");
        } catch (Exception e) {
            return "";
        }
    }

    public static String getValueByRegex(String value, String regex, boolean isGetCode) {
        try {
            var map = new HashMap<String, String>();
            List<String> list = java.util.Arrays.asList(regex.split("[｜|]"));
            list.forEach(x -> {
                var arrVal = x.split("[：:]");
                if (isGetCode) {
                    map.put(arrVal[1], arrVal[0]);
                } else {
                    map.put(arrVal[0], arrVal[1]);
                }
            });
            return map.getOrDefault(value, "");
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean checkValueByRegex(String value, String regex) {
        try {
            if (org.apache.commons.lang3.StringUtils.isBlank(value) || regex.contains(value)) {
                return true;
            }
            var map = new HashMap<String, String>();
            List<String> list = java.util.Arrays.asList(regex.split("[｜|]"));
            list.forEach(x -> {
                var arrVal = x.split("[：:]");
                map.put(arrVal[0], arrVal[1]);
            });
            return map.containsKey(value) || map.containsValue(value);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null || value.isBlank()) {
            return true;
        }
        return false;
    }

    public static String mappingField(String text, Map<String, String> values) {
        var newText = text;
        for (var entry : values.entrySet()) {
            newText = newText.replaceAll(":" + entry.getKey(), entry.getValue());
        }
        return newText;
    }
}
