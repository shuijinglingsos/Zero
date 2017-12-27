package com.elf.zero.net;

import java.util.HashMap;
import java.util.Map;

/**
 * 构建网络请求
 * Created by Lidong on 2017/12/13.
 */
public class NetRequestBuild {

    private final static int GET_MEHTOD = 1;
    private final static int POST_METHOD = 2;

    private NetRequest mNetRequest;
    private String mUrl;
    private Map<String, String> mHeader = new HashMap<>();
    private Map<String, String> mGetParams = new HashMap<>();
    private String mPostParams;
    private int mMethod;

    public static NetRequestBuild Build() {
        return new NetRequestBuild(new DefaultNetRequest());
    }

    public static NetRequestBuild Build(NetRequest request) {
        return new NetRequestBuild(request);
    }

    private NetRequestBuild(NetRequest request) {
        mNetRequest = request;
    }

    public NetRequestBuild url(String url) {
        mUrl = url;
        return this;
    }

    public NetRequestBuild header(Map<String, String> header) {
        mHeader.putAll(header);
        return this;
    }

    public NetRequestBuild get(Map<String, String> params) {
        mGetParams.putAll(params);
        mMethod = GET_MEHTOD;
        return this;
    }

    public NetRequestBuild post(String params) {
        mPostParams = params;
        mMethod = POST_METHOD;
        return this;
    }

//    public NetResponse request() throws NetException {
//        mNetRequest.setUrl(mUrl);
//        mNetRequest.setRequestHeaders(mHeader);
//        if (GET_MEHTOD == mMethod) {
//            return mNetRequest.get(mGetParams);
//        } else if (POST_METHOD == mMethod) {
//            return mNetRequest.post(mPostParams);
//        } else {
//            throw new NetException(-1, "未知的请求方法");
//        }
//    }
//
//    public NetRequest request(NetRequestListener listener) {
//        mNetRequest.setUrl(mUrl);
//        mNetRequest.setRequestHeaders(mHeader);
//        if (GET_MEHTOD == mMethod) {
//            mNetRequest.get(mGetParams, listener);
//        } else if (POST_METHOD == mMethod) {
//            mNetRequest.post(mPostParams, listener);
//        }
//        return mNetRequest;
//    }
}
