package com.elf.zero.net.http;

import java.io.IOException;

/**
 * http请求回调
 * Created by Lidong on 2018/4/8.
 */
public interface HttpCallback {

    void success(HttpResponse response);

    void failure(HttpRequest request, Exception e);
}
