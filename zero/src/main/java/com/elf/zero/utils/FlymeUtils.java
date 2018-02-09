package com.elf.zero.utils;

import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * flyme 工具
 * Created by Lidong on 2018/2/1.
 */
public class FlymeUtils {

    public static boolean isFlyme() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));

            String meizuFlymeOSFlag = properties.getProperty("ro.build.display.id");
            if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
                return false;
            }

            if (meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }
}
