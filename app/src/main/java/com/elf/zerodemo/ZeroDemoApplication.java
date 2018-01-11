package com.elf.zerodemo;

import android.app.Application;

import com.elf.zero.Zero;

/**
 * application
 * Created by Lidong on 2018/1/11.
 */
public class ZeroDemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CrashHandler.getInstance().init(this);
        Zero.init(this);
    }
}
