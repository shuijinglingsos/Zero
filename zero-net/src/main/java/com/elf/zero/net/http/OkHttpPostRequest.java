package com.elf.zero.net.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * okhttp post请求实现
 * Created by Lidong on 2018/4/16.
 */
public class OkHttpPostRequest implements HttpRequest {

    private String mUrl;
    private Map<String, String> mParams = new LinkedHashMap<>();
    private Map<String, List<String>> mHeaders = new LinkedHashMap<>();
    private Call mCall;

    @Override
    public void url(String url) {
        mUrl=url;
    }

    @Override
    public void header(String key, String value) {
        if (mHeaders.containsKey(key)) {
            mHeaders.get(key).add(value);
        } else {
            List<String> values = new ArrayList<>();
            values.add(value);
            mHeaders.put(key, values);
        }
    }

    @Override
    public void headers(Map<String, List<String>> headers) {
        mHeaders.putAll(headers);
    }

    @Override
    public HttpResponse exe() throws IOException {
        return null;
    }

    @Override
    public void exe(HttpCallback callback) {

    }

    @Override
    public void cancel() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }
}
