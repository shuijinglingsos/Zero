package com.elf.zero.net;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * 默认网络请求
 * Created by Lidong on 2017/12/6.
 */
public class DefaultNetRequest extends AbstractNetRequest {

    private HttpURLConnection mHttpURLConnection;

    @Override
    public NetResponse get(Map<String, String> params) throws NetException {

        try {
            String url = appendParams(params);
            mHttpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            mHttpURLConnection.setRequestMethod("GET");
            mHttpURLConnection.setDoInput(true);
            return buildResponse();
        } catch (Exception ex) {
            throw new NetException(-1, ex);
        }
    }

    @Override
    public NetResponse post(String params) throws NetException {
        checkUrl();
        try {
            mHttpURLConnection = (HttpURLConnection) new URL(getUrl()).openConnection();
            mHttpURLConnection.setRequestMethod("POST");
            if (!TextUtils.isEmpty(params)) {
                OutputStream os = mHttpURLConnection.getOutputStream();
                os.write(params.getBytes());
                os.flush();
            }
            return buildResponse();
        } catch (Exception ex) {
            throw new NetException(-1, ex);
        }
    }

    @Override
    public NetResponse form(Map<String, String> fields, Map<String, File> files) throws NetException {
        return null;
    }

    @Override
    public void get(final Map<String, String> params, final NetRequestListener listener) {
        new Thread(){
            @Override
            public void run() {
                try {
                    checkUrl();
                    String url = appendParams(params);
                    mHttpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    mHttpURLConnection.setRequestMethod("GET");
                    mHttpURLConnection.setDoInput(true);

                    int responseCode = mHttpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        if (listener != null) {
                            listener.onSuccess(buildResponse());
                        }
                    } else {
                        if (listener != null) {
                            listener.onFailure(new NetException(responseCode, mHttpURLConnection.getResponseMessage()));
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    if (listener != null) {
                        listener.onFailure(new NetException(-1, ex));
                    }
                }
            }
        }.start();
    }

    @Override
    public void post(String params, NetRequestListener listener) {

    }

    @Override
    public void form(Map<String, String> fields, Map<String, File> files, NetRequestListener listener) {

    }

    @Override
    public void close() {
        if (mHttpURLConnection != null) {
            mHttpURLConnection.disconnect();
        }
    }

    private NetResponse buildResponse() throws Exception {

        InputStream is = mHttpURLConnection.getInputStream();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int len;
        byte buffer[] = new byte[1024];
        while ((len = is.read(buffer)) != -1) {
            outputStream.write(buffer, 0, len);
        }
        is.close();
        outputStream.close();

        return new DefaultNetResponse(
                mHttpURLConnection.getResponseCode(),
                mHttpURLConnection.getResponseMessage(),
                outputStream.toByteArray(),
                null);
    }
}
