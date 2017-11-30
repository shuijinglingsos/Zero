package com.elf.zero.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * app工具类
 * Created by Lidong on 2017/11/30.
 */
public class AppUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取app的versionName
     * @return versionName
     */
    public static String getVersionName() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取app的versionCode
     * @return versionCode
     */
    public static int getVersionCode() {
        try {
            PackageManager pm = mContext.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取meta值
     * @param metaName meta名称
     * @return meta值
     */
    public static String getMetaValue(String metaName) {
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo appInfo = pm.getApplicationInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.metaData.getString(metaName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }
}
