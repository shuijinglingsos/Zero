package com.elf.zero.net;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认的网络连接
 * Created by Lidong on 2017/12/6.
 */
public abstract class AbstractNetRequest implements NetRequest {

    protected final String METHOD_GET = "GET";
    protected final String METHOD_POST = "POST";

    private boolean mCancel;
    private String mUrl;
    private Map<String, String> mHeaders = new HashMap<>();

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String getUrl() {
        return mUrl;
    }

    @Override
    public void setHeaders(Map<String, String> headers) {
        mHeaders = headers;
    }

    @Override
    public Map<String, String> getHeaders() {
        return mHeaders;
    }

    @Override
    public void cancel() {
        mCancel = true;
    }

    @Override
    public boolean isCancel() {
        return mCancel;
    }

    @Override
    public NetResponse get() throws NetException {
        return get(new HashMap<String, String>());
    }

    @Override
    public NetResponse get(Map<String, String> params) throws NetException {
        return request(getUrl(), METHOD_GET, params, null);
    }

    @Override
    public NetResponse post(String params) throws NetException {
        return request(getUrl(), METHOD_POST, null, params);
    }

    @Override
    public void get(NetRequestListener listener) {
        get(null, listener);
    }

    @Override
    public void get(final Map<String, String> params, final NetRequestListener listener) {
        request(getUrl(), METHOD_GET, params, null, listener);
    }

    @Override
    public void post(final String params, final NetRequestListener listener) {
        request(getUrl(), METHOD_POST, null, params, listener);
    }

    protected abstract NetResponse request(String url, String method, Map<String, String> getParams, String postParams) throws NetException;

    protected abstract void request(final String url, final String method, final Map<String, String> getParams, final String postParams, final NetRequestListener listener);

    /**
     * 追加参数
     *
     * @param url    url
     * @param params 参数集合
     * @return 返回追加参数后的url
     */
    protected String appendParams(String url, Map<String, String> params) {
        if (params != null && !params.isEmpty()) {
            if (url.contains("?")) {
                if (!url.endsWith("?")) {  //最后一位是问号
                    url += "&";
                }
            } else {
                url += "?";
            }

            int i = 0;
            for (String key : params.keySet()) {
                if (i > 0) {
                    url += "&";
                }
                url = url + key + "=" + params.get(key);
                i++;
            }
        }
        return url;
    }
}
