package io.github.iamyours.wandroid.net

import io.github.iamyours.wandroid.App
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.net.wan.WanResponse
import io.github.iamyours.wandroid.util.SP
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ApiFactory {
    inline fun <reified T> create(
        baseUrl: String,
        saveCookie: Boolean,
        noinline creator: (Int, String, Any?) -> Any
    ): T {
        val file =
            File(App.instance.cacheDir, "http-cache")
        val clientBuilder = OkHttpClient.Builder()
            .cache(Cache(file, 1024 * 1024 * 20))
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
        if (BuildConfig.DEBUG) {
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            clientBuilder.addInterceptor(loggingInterceptor)
        }
        if (saveCookie) {
            clientBuilder.cookieJar(object : CookieJar {
                override fun saveFromResponse(
                    url: HttpUrl,
                    cookies: MutableList<Cookie>
                ) {
                    if (url.toString().startsWith(
                            "https://www.wanandroid.com/user/login?"
                        )
                    ) {
                        SP.saveCookies(cookies)
                    }
                }

                override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                    return SP.getCookies()
                }
            })
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clientBuilder.build())
            .addCallAdapterFactory(LiveDataCallAdapterFactory(creator))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(T::class.java)
    }
}