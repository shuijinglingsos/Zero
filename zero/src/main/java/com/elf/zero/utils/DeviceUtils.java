package com.elf.zero.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;

/**
 * Device 工具类
 * Created by Lidong on 2017/11/30.
 */
public class DeviceUtils {

    private static Context mContext;

    public static void init(Context context) {
        mContext = context;
    }

    /**
     * 获取设备ID
     *
     * @return IMEI或MEID
     */
    public static String getDeviceId() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } else {
            return "";
        }
    }

    /**
     * 获取mac地址（有时返回02:00:00:00:00:00）
     *
     * @return mac地址
     */
    public static String getMacAddress() {
        WifiManager wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        return wm.getConnectionInfo().getMacAddress();
    }

    /**
     * 获取电话号码
     *
     * @return 电话号码
     */
    public static String getTelephoneNumber() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getLine1Number();
        } else {
            return "";
        }
    }

    /**
     * 获取网络运营商名称
     *
     * @return 网络运营商名称
     */
    public static String getNetworkOperatorName() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getNetworkOperatorName();
        } else {
            return "";
        }
    }

    /**
     * 获取设备型号
     * @return 设备型号
     */
    public static String getDeviceModel(){
        return android.os.Build.MODEL;
    }
}
