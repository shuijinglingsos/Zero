package com.elf.zero.net;

import java.util.ArrayList;
import java.util.List;
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
    private List<KeyValuePair<String>> mHeaders = new ArrayList<>();

    @Override
    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public void setRequestHeader(String key, String value) {
        mHeaders.add(new KeyValuePair<String>(key, value));
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
    public NetResponse get(List<KeyValuePair<String>> params) throws NetException {
        return request(getUrl(), METHOD_GET, params, null);
    }

    @Override
    public NetResponse post(String params) throws NetException {
        return request(getUrl(), METHOD_POST, null, params);
    }

    @Override
    public void get(List<KeyValuePair<String>> params, NetRequestListener listener) {
        request(getUrl(), METHOD_GET, params, null, listener);
    }

    @Override
    public void post(final String params, final NetRequestListener listener) {
        request(getUrl(), METHOD_POST, null, params, listener);
    }

    protected String getUrl() {
        return mUrl;
    }

    protected List<KeyValuePair<String>> getHeaders() {
        return mHeaders;
    }

    protected abstract NetResponse request(String url, String method, List<KeyValuePair<String>> getParams,
                                           String postParams) throws NetException;

    protected abstract void request(String url, String method, List<KeyValuePair<String>> getParams,
                                    String postParams, NetRequestListener listener);

    /**
     * 追加参数
     *
     * @param url    url
     * @param params 参数集合
     * @return 返回追加参数后的url
     */
    protected String appendParams(String url, List<KeyValuePair<String>> params) {
        if (params != null && !params.isEmpty()) {
            if (url.contains("?")) {
                if (!url.endsWith("?")) {  //最后一位是问号
                    url += "&";
                }
            } else {
                url += "?";
            }

            int i = 0;
            for (KeyValuePair<String> loop : params) {
                if (i > 0) {
                    url += "&";
                }
                url = url + loop.getKey() + "=" + loop.getValue();
                i++;
            }
        }
        return url;
    }
}
