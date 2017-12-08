package com.elf.zero.net;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 默认网络请求
 * Created by Lidong on 2017/12/6.
 */
public class DefaultNetRequest extends AbstractNetRequest {

    private HttpURLConnection mHttpURLConnection;


    @Override
    public NetResponse form(Map<String, String> fields, Map<String, File> files) throws NetException {
        return null;
    }

    @Override
    public void form(Map<String, String> fields, Map<String, File> files, NetRequestListener listener) {

    }

    @Override
    public void download(NetDownloadListener listener) {

    }

    @Override
    public void cancel() {
        super.cancel();
        if (mHttpURLConnection != null) {
            mHttpURLConnection.disconnect();
        }
    }

    /**
     * 发起同步网络请求
     *
     * @param url        url
     * @param method     方法
     * @param getParams  get方法参数
     * @param postParams post方法参数
     * @return 网络响应
     * @throws NetException 异常
     */
    protected NetResponse request(String url, String method, Map<String, String> getParams,
                                  String postParams) throws NetException {
        try {
            if (METHOD_GET.equals(method)) {
                url = appendParams(url, getParams);
            }
            mHttpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            mHttpURLConnection.setRequestMethod(method);
            mHttpURLConnection.setUseCaches(false);

            Map<String, String> requestHeaders = getHeaders();
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                for (String key : requestHeaders.keySet()) {
                    mHttpURLConnection.setRequestProperty(key, requestHeaders.get(key));
                }
            }

            if (METHOD_POST.equals(method)) {
                mHttpURLConnection.setDoOutput(true);
                if (!TextUtils.isEmpty(postParams)) {
                    OutputStream os = mHttpURLConnection.getOutputStream();
                    os.write(postParams.getBytes());
                    os.flush();
                }
            }

            int responseCode = mHttpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = mHttpURLConnection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                is.close();
                outputStream.close();

                Map<String, String> responseHeader = new HashMap<>();
                Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
                if (map != null && !map.isEmpty()) {
                    for (String key : map.keySet()) {
                        responseHeader.put(key, map.get(key).get(0));
                    }
                }

                return new DefaultNetResponse(
                        mHttpURLConnection.getResponseCode(),
                        mHttpURLConnection.getResponseMessage(),
                        outputStream.toByteArray(),
                        responseHeader);
            } else {
                throw new NetException(responseCode, mHttpURLConnection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetException(-1, e);
        }
    }

    /**
     * 发起异步网络请求
     *
     * @param url        url
     * @param method     method
     * @param getParams  get方法参数
     * @param postParams post方法参数
     * @param listener   监听回调
     */
    protected void request(final String url, final String method, final Map<String, String> getParams, final String postParams, final NetRequestListener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    NetResponse netResponse = request(url, method, getParams, postParams);
                    if (isCancel()) {
                        return;
                    }
                    if (listener != null) {
                        listener.onSuccess(netResponse);
                    }
                } catch (NetException ex) {
                    ex.printStackTrace();
                    if (isCancel()) {
                        return;
                    }
                    if (listener != null) {
                        listener.onFailure(ex);
                    }
                }
            }
        }.start();
    }
}
