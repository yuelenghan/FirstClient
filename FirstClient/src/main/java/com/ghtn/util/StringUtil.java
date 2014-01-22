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
        if (s.trim().length() > limit) {
            s = s.trim().substring(0, limit) + "...";
        }
        return s;
    }

    /**
     * 从字符串中得到int值(字符串格式 -- 字符串:int)
     *
     * @param s
     * @return
     */
    public static int getIntValue(String s) {
        s = s.substring(s.indexOf(":") + 1).trim();
        return Integer.parseInt(s);
    }

    /**
     * 返回字符串的值, 如果字符串为空串, 返回"", 否则返回本身
     *
     * @param s
     * @return
     */
    public static String getStringValue(String s) {
        return isNullStr(s) ? "" : s.trim();
    }

}
