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

}
