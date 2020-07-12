package io.github.iamyours.wandroid.net;

import java.io.IOException;

import io.github.iamyours.wandroid.extension.StringKt;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RedirectInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        okhttp3.Request request = chain.request();
        Response response = chain.proceed(request);
        int code = response.code();
        if (code == 307 || code == 301) {
            //获取重定向的地址
            String location = response.headers().get("Location");
            if (!location.startsWith("http")) {
                location =
                        request.url().scheme() + "://" + request.url().host() + location;
            }
            StringKt.logE("重定向地址：,location = " + location);
            //重新构建请求
            Request newRequest = request.newBuilder().url(location)
                    .header("user-agent",request.header("user-agent"))
                    .build();
            response = chain.proceed(newRequest);
        }
        return response;
    }
}