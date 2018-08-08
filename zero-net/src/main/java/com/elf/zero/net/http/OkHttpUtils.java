package com.elf.zero.net.http;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * okhttp 工具类
 * Created by Lidong on 2018/4/10.
 */
public class OkHttpUtils {

    public static HttpResponse convertResponse(Response response) throws IOException {
        ResponseBody responseBody = response.body();
        byte[] body = null;
        if (response.isSuccessful() && responseBody != null) {
            body = responseBody.bytes();
        }

        return new HttpResponse.Build()
                .code(response.code())
                .body(body)
                .message(response.message())
                .headers(response.headers().toMultimap())
                .build();
    }
}
