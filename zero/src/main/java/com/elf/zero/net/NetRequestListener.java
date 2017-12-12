package com.elf.zero.net;

/**
 * 异步网络请求监听（此类内的回调方法都是在异步线程）
 * Created by Lidong on 2017/12/6.
 */
public interface NetRequestListener {

    void onSuccess(NetResponse response);

    void onFailure(NetException exception);
}
