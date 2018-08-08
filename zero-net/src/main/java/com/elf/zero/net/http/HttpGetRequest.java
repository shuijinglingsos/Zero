package com.elf.zero.net.http;

import java.util.Map;

/**
 * http Get请求
 * Created by Lidong on 2018/4/8.
 */
public interface HttpGetRequest extends HttpRequest {

    void param(String key, String value);

    void params(Map<String, String> params);
}
