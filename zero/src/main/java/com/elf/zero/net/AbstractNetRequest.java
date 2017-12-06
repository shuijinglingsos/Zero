package com.elf.zero.net;

import android.text.TextUtils;

import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认的网络连接
 * Created by Lidong on 2017/12/6.
 */
public abstract class AbstractNetRequest implements NetRequest {

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
    public NetResponse get() throws NetException {
        return get(new HashMap<String, String>());
    }

    @Override
    public void get(NetRequestListener listener) {
        get(null, listener);
    }

    /**
     * 校验url
     * @throws NetException url不是合法
     */
    protected void checkUrl() throws NetException {
        if (TextUtils.isEmpty(getUrl())) {
            throw new NetException(-1, new IllegalArgumentException("url is empty:" + getUrl()));
        }
    }

    /**
     * 追加参数
     * @param params 参数集合
     * @return 返回追加参数后的url
     */
    protected String appendParams(Map<String, String> params) {
        String url = getUrl();
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
