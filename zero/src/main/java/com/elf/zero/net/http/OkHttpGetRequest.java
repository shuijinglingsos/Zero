package com.elf.zero.net.http;

import android.text.TextUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * okhttp实现
 * Created by Lidong on 2018/4/8.
 */
public class OkHttpGetRequest implements HttpGetRequest {

    private String mUrl;
    private Map<String, String> mParams = new LinkedHashMap<>();
    private Map<String, List<String>> mHeaders = new LinkedHashMap<>();
    private Call mCall;

    @Override
    public void param(String key, String value) {
        mParams.put(key, value);
    }

    @Override
    public void params(Map<String, String> params) {
        mParams.putAll(params);
    }

    @Override
    public void url(String url) {
        mUrl = url;
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
        mCall = callBuild(appendParam(mUrl, mParams));
        Response response = mCall.execute();
        return OkHttpUtils.convertResponse(response);
    }

    @Override
    public void exe(final HttpCallback callback) {
        mCall = callBuild(appendParam(mUrl, mParams));
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.failure(OkHttpGetRequest.this, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (callback != null) {
                    callback.success(OkHttpUtils.convertResponse(response));
                }
            }
        });
    }

    @Override
    public void cancel() {
        if (mCall != null && !mCall.isCanceled()) {
            mCall.cancel();
        }
    }

    private Call callBuild(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        return client.newCall(request);
    }

    private String appendParam(String url, Map<String, String> param) {

        if (TextUtils.isEmpty(url)) {
            return "";
        }

        if (param == null || param.isEmpty()) {
            return url;
        }

        StringBuilder finalUrl = new StringBuilder(url);
        if (!url.contains("?")) {
            finalUrl.append("?");
        }

        int i = 0;
        for (String key : param.keySet()) {
            if (i > 0) {
                finalUrl.append("&");
            }
            finalUrl.append(key).append("=").append(param.get(key));
            i++;
        }
        return finalUrl.toString();
    }
}
