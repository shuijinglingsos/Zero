package com.elf.zero.net;

import android.text.TextUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedInputStream;
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
import java.util.zip.GZIPInputStream;

/**
 * 默认网络请求
 * Created by Lidong on 2017/12/6.
 */
public class DefaultNetRequest extends AbstractNetRequest {

    private HttpURLConnection mHttpURLConnection;


    @SuppressWarnings("deprecation")
    @Override
    public NetResponse form(Map<String, String> fields, Map<String, File> files) throws NetException {

        try {
            MultipartEntity multipartEntity = new MultipartEntity();
            for (String key : fields.keySet()) {
                multipartEntity.addPart(key, new StringBody(fields.get(key)));
            }
            for (String key : files.keySet()) {
                multipartEntity.addPart(key, new FileBody(files.get(key)));
            }
            // 请求
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(getUrl());
            post.setEntity(multipartEntity);

            Map<String, String> requestHeaders = getHeaders();
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                for (String key : requestHeaders.keySet()) {
                    post.addHeader(key, requestHeaders.get(key));
                }
            }
            HttpResponse response = httpClient.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                InputStream is = getInputStream(response, response.getEntity());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int len;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, len);
                }
                is.close();
                outputStream.close();

                Map<String, String> responseHeader = new HashMap<>();
                Header[] map = response.getAllHeaders();
                if (map != null && map.length > 0) {
                    for (Header header : map) {
                        responseHeader.put(header.getName(), header.getValue());
                    }
                }

                return new DefaultNetResponse(
                        responseCode,
                        response.getStatusLine().getReasonPhrase(),
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

    @SuppressWarnings("deprecation")
    private InputStream getInputStream(HttpResponse response, HttpEntity entity) throws IOException {

        InputStream is = entity.getContent();
        boolean gzip = false;
        if (response.containsHeader("Content-Encoding")) {// [Content-Language:
            // zh-CN,
            // Content-Encoding:
            // gzip,
            // Content-Length:
            // 10,
            // Server:
            // Jetty(6.1.26)]
            String gzipStr = response.getHeaders("Content-Encoding")[0]
                    .getValue();
            gzip = gzipStr.equals("gzip");
        }
        if (gzip) {// 头文件支持压缩 -- 然后检测数据格式是否为GZIP压缩
            BufferedInputStream bis = new BufferedInputStream(is);
            bis.mark(2);
            // 取前两个字节
            byte[] header = new byte[2];
            int result = bis.read(header);
            // reset输入流到开始位置
            bis.reset();
            // 判断是否是GZIP格式
            int headerData = getShort(header);
            // Gzip 流 的前两个字节是 0x1f8b
            if (result != -1 && headerData == 0x1f8b) {
                // DebugUtil.debug(TAG, " use GZIPInputStream  ");
                is = new GZIPInputStream(bis);
            } else {
                // DebugUtil.debug(TAG, " not use GZIPInputStream");
                is = bis;
            }
        }
        return is;
    }

    private int getShort(byte[] data) {
        return ((data[0] << 8) | data[1] & 0xFF);
    }

    @Override
    public void form(final Map<String, String> fields, final Map<String, File> files, final NetRequestListener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    NetResponse netResponse = form(fields, files);
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
