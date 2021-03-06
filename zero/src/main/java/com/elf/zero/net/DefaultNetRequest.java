package com.elf.zero.net;

import android.text.TextUtils;

import com.elf.zero.utils.StreamUtils;

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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
    public NetResponse form(List<KeyValuePair<String>> fields, List<KeyValuePair<File>> files) throws NetException {

        try {
            MultipartEntity multipartEntity = new MultipartEntity();
            if(fields!=null && !fields.isEmpty()) {
                for (KeyValuePair<String> loop : fields) {
                    multipartEntity.addPart(loop.getKey(), new StringBody(loop.getValue()));
                }
            }
            if(files!=null && !files.isEmpty()) {
                for (KeyValuePair<File> loop : files) {
                    multipartEntity.addPart(loop.getKey(), new FileBody(loop.getValue()));
                }
            }
            // 请求
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(getUrl());
            post.setEntity(multipartEntity);

            List<KeyValuePair<String>> requestHeaders = getHeaders();
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                for (KeyValuePair<String> loop : requestHeaders) {
                    post.addHeader(loop.getKey(), loop.getValue());
                }
            }
            HttpResponse response = httpClient.execute(post);
            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode == 200) {
                InputStream is = getInputStream(response, response.getEntity());
                byte[] result = StreamUtils.streamToByteArray(is);
                is.close();

                List<KeyValuePair<String>> responseHeader = new ArrayList<>();
                Header[] map = response.getAllHeaders();
                if (map != null && map.length > 0) {
                    for (Header header : map) {
                        responseHeader.add(new KeyValuePair<>(header.getName(), header.getValue()));
                    }
                }

                return new DefaultNetResponse(
                        responseCode,
                        response.getStatusLine().getReasonPhrase(),
                        result,
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
        if (response.containsHeader("Content-Encoding")) {
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
                is = new GZIPInputStream(bis);
            } else {
                is = bis;
            }
        }
        return is;
    }

    private int getShort(byte[] data) {
        return ((data[0] << 8) | data[1] & 0xFF);
    }

    @Override
    public void form(final List<KeyValuePair<String>> fields, final List<KeyValuePair<File>> files,
                     final NetRequestListener listener) {
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
    public NetResponse download(File saveFile) throws NetException {
        try {
            mHttpURLConnection = (HttpURLConnection) new URL(getUrl()).openConnection();
            mHttpURLConnection.setRequestMethod(METHOD_GET);

            List<KeyValuePair<String>> requestHeaders = getHeaders();
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                for (KeyValuePair<String> loop : requestHeaders) {
                    mHttpURLConnection.setRequestProperty(loop.getKey(), loop.getValue());
                }
            }

            int responseCode = mHttpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = mHttpURLConnection.getInputStream();

                FileOutputStream fos = new FileOutputStream(saveFile);
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();

                List<KeyValuePair<String>> responseHeader = new ArrayList<>();
                Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
                if (map != null && !map.isEmpty()) {
                    for (String key : map.keySet()) {
                        responseHeader.add(new KeyValuePair<>(key, map.get(key).get(0)));
                    }
                }

                return new DefaultNetResponse(
                        mHttpURLConnection.getResponseCode(),
                        mHttpURLConnection.getResponseMessage(),
                        null,
                        responseHeader);

            } else {
                throw new NetException(responseCode, mHttpURLConnection.getResponseMessage());
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetException(-1, e);
        }
    }

    @Override
    public void download(final File saveFile, final NetDownloadListener listener) {
        new Thread() {
            @Override
            public void run() {
                try {
                    mHttpURLConnection = (HttpURLConnection) new URL(getUrl()).openConnection();
                    mHttpURLConnection.setRequestMethod(METHOD_GET);

                    List<KeyValuePair<String>> requestHeaders = getHeaders();
                    if (requestHeaders != null && !requestHeaders.isEmpty()) {
                        for (KeyValuePair<String> loop : requestHeaders) {
                            mHttpURLConnection.setRequestProperty(loop.getKey(), loop.getValue());
                        }
                    }

                    int responseCode = mHttpURLConnection.getResponseCode();
                    if (responseCode == 200) {
                        InputStream is = mHttpURLConnection.getInputStream();

                        int length = mHttpURLConnection.getContentLength();

                        FileOutputStream fos = new FileOutputStream(saveFile);
                        byte[] buffer = new byte[1024];
                        int readCount = 0, len;
                        while ((len = is.read(buffer)) != -1) {
                            readCount += len;
                            fos.write(buffer, 0, len);
                            if (listener != null) {
                                listener.onProgress(readCount, length);
                            }
                        }
                        is.close();
                        fos.close();

                        List<KeyValuePair<String>> responseHeader = new ArrayList<>();
                        Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
                        if (map != null && !map.isEmpty()) {
                            for (String key : map.keySet()) {
                                responseHeader.add(new KeyValuePair<>(key, map.get(key).get(0)));
                            }
                        }

                        if (listener != null) {
                            listener.onSuccess(saveFile, responseHeader);
                        }

                    } else {
                        if (listener != null) {
                            listener.onFailure(new NetException(responseCode, mHttpURLConnection.getResponseMessage()));
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.onFailure(new NetException(-1, e));
                    }
                }
            }
        }.start();
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
    protected NetResponse request(String url, String method, List<KeyValuePair<String>> getParams,
                                  String postParams) throws NetException {
        try {
            if (METHOD_GET.equals(method)) {
                url = appendParams(url, getParams);
            }

            URL url_o = new URL(url);
            mHttpURLConnection = (HttpURLConnection) url_o.openConnection();
            mHttpURLConnection.setRequestMethod(method);
            mHttpURLConnection.setDoInput(true);
            mHttpURLConnection.setDoOutput(true);
            mHttpURLConnection.setUseCaches(false);

            mHttpURLConnection.setRequestProperty("Content-Type", "application/text");

            List<KeyValuePair<String>> requestHeaders = getHeaders();
            if (requestHeaders != null && !requestHeaders.isEmpty()) {
                for (KeyValuePair<String> loop : requestHeaders) {
                    mHttpURLConnection.setRequestProperty(loop.getKey(), loop.getValue());
                }
            }

            if (METHOD_POST.equals(method)) {
                if (!TextUtils.isEmpty(postParams)) {
                    OutputStream os = mHttpURLConnection.getOutputStream();
                    os.write(postParams.getBytes());
                    os.flush();
                }
            }

            int responseCode = mHttpURLConnection.getResponseCode();
            if (responseCode == 200) {
                InputStream is = mHttpURLConnection.getInputStream();
                byte[] result = StreamUtils.streamToByteArray(is);
                is.close();

                List<KeyValuePair<String>> responseHeader = new ArrayList<>();
                Map<String, List<String>> map = mHttpURLConnection.getHeaderFields();
                if (map != null && !map.isEmpty()) {
                    for (String key : map.keySet()) {
                        responseHeader.add(new KeyValuePair<>(key, map.get(key).get(0)));
                    }
                }

                return new DefaultNetResponse(
                        mHttpURLConnection.getResponseCode(),
                        mHttpURLConnection.getResponseMessage(),
                        result,
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
    protected void request(final String url, final String method, final List<KeyValuePair<String>> getParams,
                           final String postParams, final NetRequestListener listener) {
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
