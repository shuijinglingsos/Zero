package com.elf.zero.net.http;

import java.util.Map;

/**
 * http post请求
 * Created by Lidong on 2018/4/8.
 */
public interface HttpPostRequest extends HttpRequest {

    void body(String body);
}
