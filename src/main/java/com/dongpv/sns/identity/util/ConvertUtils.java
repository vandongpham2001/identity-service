package com.dongpv.sns.identity.util;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConvertUtils {
    public static List<String> asList(String[] val) {
        return java.util.Arrays.asList(val);
    }

    public static List<Long> asList(Long[] val) {
        return java.util.Arrays.asList(val);
    }

    public static List<Long> asLongList(String stringList) {
        return asList(stringList.split(",")).stream().map(Long::parseLong).toList();
    }

    public static List<String> asStringList(String stringList) {
        return asList(stringList.split(","));
    }

    public static String[] asStringArray(List<String> list) {
        String[] array = new String[list.size()];
        array = list.toArray(array);
        return array;
    }

    public static Long[] asLongArray(List<Long> list) {
        Long[] array = new Long[list.size()];
        array = list.toArray(array);
        return array;
    }

    public static Long[] asLongArray(String stringList) {
        return asLongArray(asLongList(stringList));
    }

    public static String[] asStringArray(String stringList) {
        return stringList.split(",");
    }

    public static String asString(List<String> list) {
        return String.join(",", list);
    }
}
