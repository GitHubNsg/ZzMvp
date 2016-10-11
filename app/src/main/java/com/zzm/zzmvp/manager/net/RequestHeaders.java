package com.zzm.zzmvp.manager.net;

import com.zzm.zzmvp.util.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by itzhong on 2016/7/28.
 */
public class RequestHeaders {

    /**
     * 添加所有请求头
     * @return
     */
    public static Interceptor addHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("access_token", SPUtils.getString(SPUtils.TOKEN,""))
                        .addHeader("device_id", SPUtils.getDefaultString(SPUtils.IMEI,""))
                        .build();
                return chain.proceed(request);
            }
        };
    }

    public static Map<String,String> headers(){
        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", SPUtils.getString(SPUtils.TOKEN,""));
        map.put("Content-Type", "application/json; charset=utf-8");
        map.put("device_id", SPUtils.getDefaultString(SPUtils.IMEI,""));
        return map;
    }

}
