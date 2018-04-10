package com.elf.zero.net.http;

import java.io.File;
import java.util.Map;

/**
 * http form 请求
 * Created by Lidong on 2018/4/8.
 */
public interface HttpFormRequest extends HttpRequest {

    void param(String key, String value);

    void params(Map<String, String> params);

    void file(String key, String value, File file);
}
