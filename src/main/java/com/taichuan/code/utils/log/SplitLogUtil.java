package com.taichuan.code.utils.log;

import android.util.Log;

/**
 * @author gui
 * @date 2020-03-03
 * 用户过长数据的打印，让logcat能够打印完全
 */
public class SplitLogUtil {
    public static void d(String tag, String msg) {
        String[] strArray = str_split(msg, 3000);
        for (String str : strArray) {
            Log.d(tag, "log: " + str);
        }
    }

    private static String[] str_split(String str, int length) {
        int len = str.length();
        String[] arr = new String[(len + length - 1) / length];

        for (int i = 0; i < len; i += length) {
            int n = len - i;
            if (n > length) {
                n = length;
            }
            arr[i / length] = str.substring(i, i + n);

        }
        return arr;
    }

}
