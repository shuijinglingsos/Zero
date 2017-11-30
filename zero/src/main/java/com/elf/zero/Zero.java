package com.elf.zero;

import android.content.Context;

import com.elf.zero.utils.AppUtils;
import com.elf.zero.utils.DeviceUtils;
import com.elf.zero.utils.NetworkUtils;

/**
 * zero
 * Created by Lidong on 2017/11/30.
 */
public class Zero {

    /**
     * 初始化
     *
     * @param context application context
     */
    public static void init(Context context) {
        AppUtils.init(context);
        DeviceUtils.init(context);
        NetworkUtils.init(context);
    }
}
