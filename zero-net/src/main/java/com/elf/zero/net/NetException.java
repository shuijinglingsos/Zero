package com.elf.zero.net;

import java.net.UnknownHostException;

/**
 * 网络连接异常
 * Created by Lidong on 2017/12/6.
 */
public class NetException extends Exception {

    private int mResponseCode;
    private Exception mException;

    public NetException(int mResponseCode, String msg) {
        this(mResponseCode, new Exception(msg));
    }

    public NetException(int responseCode, Exception e) {
        mResponseCode = responseCode;
        mException = e;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    @Override
    public void printStackTrace() {
        if (mException == null) {
            super.printStackTrace();
        } else {
            mException.printStackTrace();
        }
    }

    @Override
    public String getMessage() {
        if (mException == null) {
            return super.getMessage();
        } else {
            return mException.getMessage();
        }
    }

    private String getExceptionMessage(Exception e) {
        if (e instanceof UnknownHostException) {
            return "访问服务器失败，请检查网络连接。";
        } else {
            return e.getMessage();
        }
    }
}
