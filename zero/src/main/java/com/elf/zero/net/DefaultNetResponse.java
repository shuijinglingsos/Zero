package com.elf.zero.net;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * 默认的网络响应
 * Created by Lidong on 2017/12/6.
 */
public class DefaultNetResponse implements NetResponse {

    private int mResponseCode;
    private String mResponseMessage;
    private byte[] mResponseContent;
    private Map<String, String> mHeaders;

    public DefaultNetResponse(int code, String message, byte[] content, Map<String, String> headers) {
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
    public Map<String, String> getHeader() {
        return mHeaders;
    }
}
