package com.elf.zero.net.http;

import java.util.List;
import java.util.Map;

/**
 * http响应
 * Created by Lidong on 2018/4/8.
 */
public class HttpResponse {

    private int code;
    private byte[] body;
    private String message;
    private Map<String, List<String>> headers;

    public boolean isSuccessful() {
        return this.code >= 200 && this.code < 300;
    }

    public int code() {
        return code;
    }

    public byte[] body() {
        return body;
    }

    public String message() {
        return message;
    }

    public String header(String key) {
        if (headers != null && headers.containsKey(key)) {
            List<String> values = headers.get(key);
            if (values != null && values.size() > 0) {
                return values.get(0);
            }
        }
        return null;
    }

    public List<String> headerList(String key) {
        return headers.get(key);
    }

    public Map<String, List<String>> headers() {
        return headers;
    }

    public static class Build {

        private HttpResponse mHttpResponse;

        public Build() {
            mHttpResponse = new HttpResponse();
        }

        public Build body(byte[] value) {
            mHttpResponse.body = value;
            return this;
        }

        public Build code(int code) {
            mHttpResponse.code = code;
            return this;
        }

        public Build message(String msg) {
            mHttpResponse.message = msg;
            return this;
        }

        public Build headers(Map<String, List<String>> headers) {
            mHttpResponse.headers = headers;
            return this;
        }

        public HttpResponse build() {
            return mHttpResponse;
        }
    }
}

