package com.elf.zero.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * 网络工具类
 * Created by Lidong on 2017/11/30.
 */
public class NetworkUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 是否有可用的网络连接
     * @return 有可用的网络连接返回true，否则返回false
     */
    public static boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
