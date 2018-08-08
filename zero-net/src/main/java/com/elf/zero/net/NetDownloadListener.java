package com.elf.zero.net;

import java.io.File;
import java.util.List;

/**
 * 下载监听（此类内的回调方法都是在异步线程）
 * Created by Lidong on 2017/12/8.
 */
public interface NetDownloadListener {

    void onProgress(int readCount, int totalCount);

    void onFailure(NetException exception);

    void onSuccess(File file, List<KeyValuePair<String>> headers);
}
