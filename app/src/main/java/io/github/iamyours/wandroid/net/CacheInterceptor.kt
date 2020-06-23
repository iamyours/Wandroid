package io.github.iamyours.wandroid.net

import io.github.iamyours.wandroid.App
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val oldReq = chain.request()
        val req = if (App.isNetworkConnected()) oldReq else oldReq.newBuilder()
            .cacheControl(CacheControl.FORCE_CACHE).build()
        return chain.proceed(
            req
        )
    }
}