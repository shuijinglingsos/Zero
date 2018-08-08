package com.elf.zero.net;

import java.util.List;
import java.util.Map;

/**
 * 默认的网络响应
 * Created by Lidong on 2017/12/6.
 */
public class DefaultNetResponse implements NetResponse {

    private int mResponseCode;
    private String mResponseMessage;
    private byte[] mResponseContent;
    private List<KeyValuePair<String>> mHeaders;

    public DefaultNetResponse(int code, String message, byte[] content, List<KeyValuePair<String>> headers) {
        mResponseCode = code;
        mResponseMessage = message;
        mResponseContent = content;
        mHeaders = headers;
    }

    @Override
    public byte[] getResponseContent() {
        return mResponseContent;
    }

    @Override
    public int getResponseCode() {
        return mResponseCode;
    }

    @Override
    public String getResponseMessage() {
        return mResponseMessage;
    }

    @Override
    public List<KeyValuePair<String>> getHeader() {
        return mHeaders;
    }
}
