package com.elf.zero.net;

/**
 * 网络连接监听
 * Created by Lidong on 2017/12/6.
 */
public interface NetRequestListener {

    void onSuccess(NetResponse response);

    void onFailure(NetException exception);
}
