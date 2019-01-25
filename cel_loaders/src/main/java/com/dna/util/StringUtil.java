package com.dna.util;

/**
 * @author Cristian Laiun

 */
public class StringUtil {
    public static String trim(String s) {
        return s != null ? s.trim() : "";
    }

    public static boolean isEmpty(String s) {
        return s == null || "".equals(s);
    }

    public static String toEmptyIfNull(String s) {
        return s == null ? "" : s;
    }

}
