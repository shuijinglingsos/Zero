package com.elf.zero.net;

import java.io.File;
import java.util.List;

/**
 * 网络连接
 * Created by Lidong on 2017/12/6.
 */
public interface NetRequest {

    void setUrl(String url);

    void setRequestHeader(String key,String value);

    NetResponse get(List<KeyValuePair<String>> params) throws NetException;

    NetResponse post(String params) throws NetException;

    NetResponse form(List<KeyValuePair<String>> fields, List<KeyValuePair<File>> files) throws NetException;

    NetResponse download(File saveFile) throws NetException;

    void get(List<KeyValuePair<String>> params, NetRequestListener listener);

    void post(String params, NetRequestListener listener);

    void form(List<KeyValuePair<String>> fields, List<KeyValuePair<File>> files, NetRequestListener listener);

    void download(File saveFile, NetDownloadListener listener);

    void cancel();

    boolean isCancel();
}
