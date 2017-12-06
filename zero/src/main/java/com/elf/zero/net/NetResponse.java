package com.elf.zero.net;

import java.io.InputStream;
import java.util.Map;

/**
 * 网络连接 响应内容
 * Created by Lidong on 2017/12/6.
 */
public interface NetResponse {

    byte[] getResponseContent();

    int getResponseCode();

    String getResponseMessage();

    Map<String, String> getHeader();
}
