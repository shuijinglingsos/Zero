package com.elf.zero.net.http;

/**
 * http 进度回调
 * Created by Lidong on 2018/4/8.
 */
public interface HttpProgressCallback extends HttpCallback {

    void progress(long readCount, long totalCount);

}
