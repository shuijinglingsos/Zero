package com.elf.zero.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * miui工具类
 * Created by Lidong on 2018/2/1.
 */
public class MiUIUtils {

    private static final String KEY_MIUI_VERSION_CODE = "ro.miui.ui.version.code";
    private static final String KEY_MIUI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final String KEY_MIUI_INTERNAL_STORAGE = "ro.miui.internal.storage";

    public static boolean isMiUI(){
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            return properties.getProperty(KEY_MIUI_VERSION_CODE, null) != null
                    || properties.getProperty(KEY_MIUI_VERSION_NAME, null) != null
                    || properties.getProperty(KEY_MIUI_INTERNAL_STORAGE, null) != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
