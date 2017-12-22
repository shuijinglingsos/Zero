package com.elf.zero.utils;

import android.util.Log;

/**
 * 日志工具
 * Created by Lidong on 2017/12/22.
 */

public class LogUtils {

    public static void v(String tag, String msg) {
        Log.d("zero-lib", tag + " " + msg);
    }
}
