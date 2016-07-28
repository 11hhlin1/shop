package com.gjj.thirdaccess;

public class QStringUtil {
    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return false;
        }
        return true;
    }
}
