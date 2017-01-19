package com.hss01248.http.okhttp;


import com.hss01248.http.config.NetDefaultConfig;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * Created by Administrator on 2016/9/22 0022.
 */
public class UseragentInterceptor implements Interceptor {

    public Response intercept(Chain chain) throws IOException {
       Request request =  chain.request();
      request =   request.newBuilder().addHeader("User-Agent", NetDefaultConfig.USER_AGENT).build();
        return chain.proceed(request);
    }
}
