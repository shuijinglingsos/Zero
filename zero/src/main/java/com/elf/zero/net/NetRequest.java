package com.elf.zero.net;

import java.io.File;
import java.util.Map;

/**
 * 网络连接
 * Created by Lidong on 2017/12/6.
 */
public interface NetRequest {

    void setUrl(String url);

    void setRequestHeaders(Map<String, String> headers);

    NetResponse get(Map<String, String> params) throws NetException;

    NetResponse post(String params) throws NetException;

    NetResponse form(Map<String, String> fields, Map<String, File> files) throws NetException;

    void get(Map<String, String> params, NetRequestListener listener);

    void post(String params, NetRequestListener listener);

    void form(Map<String, String> fields, Map<String, File> files, NetRequestListener listener);

    void download(File saveFile, NetDownloadListener listener);

    void cancel();

    boolean isCancel();
}
