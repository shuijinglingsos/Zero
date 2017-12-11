package com.elf.zero.net;

import java.io.File;
import java.util.Map;

/**
 * 下载监听
 * Created by Lidong on 2017/12/8.
 */
public interface NetDownloadListener {

    void onProgress(int readCount, int totalCount);

    void onFailure(NetException exception);

    void onSuccess(File file, Map<String, String> headers);
}
