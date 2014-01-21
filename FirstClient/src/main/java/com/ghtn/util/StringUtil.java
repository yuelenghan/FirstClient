package com.ghtn.util;

/**
 * Created by Administrator on 14-1-10.
 */
public class StringUtil {

    public static boolean isNullStr(String s) {
        if (s == null || s.trim().equals("") || s.trim().toLowerCase().equals("null")) {
            return true;
        }
        return false;
    }

    /**
     * 处理长字符串
     *
     * @param s     长字符串
     * @param limit 字符串最大长度
     * @return 处理之后的字符串
     */
    public static String processLongStr(String s, int limit) {
        if (s.length() > limit) {
            s = s.substring(0, limit) + "...";
        }
        return s;
    }

}
