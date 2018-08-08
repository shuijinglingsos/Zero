package com.elf.zero.net.http;

import java.util.List;
import java.util.Map;

/**
 * http帮助类
 * Created by Lidong on 2018/4/8.
 */
public class HttpBuild {

    public void get() {
        HttpRequest request = new HttpBuild.Get()
                .url("http://www.baidu.com")
                .header("1", "333")
                .param("2", "2")
                .build();
        request.exe(new HttpCallback() {
            @Override
            public void success(HttpResponse response) {

            }

            @Override
            public void failure(HttpRequest request, Exception e) {

            }
        });
    }

    public static class Get {

        private HttpGetRequest request;

        public Get() {
            request = new OkHttpGetRequest();
        }

        public Get url(String url) {
            request.url(url);
            return this;
        }

        public Get header(String key, String value) {
            request.header(key, value);
            return this;
        }

        public Get headers(Map<String, List<String>> headers) {
            request.headers(headers);
            return this;
        }

        public Get param(String key, String value) {
            request.param(key, value);
            return this;
        }

        public Get params(Map<String, String> params) {
            request.params(params);
            return this;
        }

        public HttpRequest build() {
            return request;
        }
    }
}
