package com.elf.zero.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络工具类
 * Created by Lidong on 2017/11/30.
 */
public class NetworkUtils {

    public final static String NETWORK_TYPE_UNKNOWN = "unknown";
    public final static String NETWORK_TYPE_WIFI = "WIFI";
    public final static String NETWORK_TYPE_2G = "2G";
    public final static String NETWORK_TYPE_3G = "3G";
    public final static String NETWORK_TYPE_4G = "4G";

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 是否有可用的网络连接
     *
     * @return 如果有可用的网络连接返回true，否则返回false
     */
    public static boolean isNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable();
    }

    /**
     * 是否是wifi网络
     *
     * @return 如果是wifi网络返回true，否则返回false
     */
    public static boolean isWifiConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * 是否是移动网络
     *
     * @return 如果是移动网络返回true，否则返回false
     */
    public static boolean isMobileConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    /**
     * 获取网络类型
     *
     * @return WIFI {@link #NETWORK_TYPE_WIFI} <br/>2G {@link #NETWORK_TYPE_2G} <br/>
     * 3G {@link #NETWORK_TYPE_3G} <br/> 4G {@link #NETWORK_TYPE_4G} <br/>
     * 未知网络返回 {@link #NETWORK_TYPE_UNKNOWN}
     */
    public static String getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                return NETWORK_TYPE_WIFI;
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                int subtype = networkInfo.getSubtype();
                switch (subtype) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        return NETWORK_TYPE_2G;

                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        return NETWORK_TYPE_3G;

                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        return NETWORK_TYPE_4G;

                    default:
                        String subTypeName = networkInfo.getSubtypeName();
                        if (subTypeName.equalsIgnoreCase("TD-SCDMA")
                                || subTypeName.equalsIgnoreCase("WCDMA")
                                || subTypeName.equalsIgnoreCase("CDMA2000")) {
                            return NETWORK_TYPE_3G;
                        }
                        break;
                }
            }
        }
        return NETWORK_TYPE_UNKNOWN;
    }
}
