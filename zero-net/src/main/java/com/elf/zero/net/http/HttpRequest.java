package com.elf.zero.net.http;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * http请求接口
 * Created by Lidong on 2018/4/8.
 */
public interface HttpRequest {

    void url(String url);

    void header(String key, String value);

    void headers(Map<String, List<String>> headers);

    HttpResponse exe() throws IOException;

    void exe(HttpCallback callback);

    void cancel();
}
