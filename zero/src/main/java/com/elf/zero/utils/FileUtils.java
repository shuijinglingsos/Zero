package com.elf.zero.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;

/**
 * 文件工具类
 * Created by Lidong on 2018/3/12.
 */
public class FileUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取缓存目录
     *
     * @return 缓存目录，优先获取sd卡缓存目录，否则获取内部存储缓存目录
     */
    public static String getCacheDir() {
        String path = "";
        if (Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            File file = mContext.getExternalCacheDir();
            if (file != null) {
                path = file.getPath();
            }
        }

        if (TextUtils.isEmpty(path)) {
            path = mContext.getCacheDir().getPath();
        }
        return path;
    }
}
