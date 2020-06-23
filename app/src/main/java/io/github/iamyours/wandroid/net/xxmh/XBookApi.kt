package io.github.iamyours.wandroid.net.xxmh

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.App
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.net.CacheInterceptor
import io.github.iamyours.wandroid.vo.xxmh.*
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.File
import java.util.concurrent.TimeUnit

interface XBookApi {
    companion object {

        fun get(): XBookApi {
            return get(
                "http://api-service.live:8080/"
            )
        }

        fun get(url: String): XBookApi {
            val file =
                File(App.instance.cacheDir, "http-cache")
            val clientBuilder = OkHttpClient.Builder()
                .cache(Cache(file,1024 * 1024 * 20))
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
            clientBuilder.addInterceptor(CacheInterceptor())
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)
            }

            return Retrofit.Builder()
                .baseUrl(url)
                .client(clientBuilder.build())
                .addCallAdapterFactory(XLiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(XBookApi::class.java)
        }
    }

    @GET("xbook/bookPage?size=20")
    fun bookPage(@Query("page") page: Int): LiveData<XResponse<XPage<XBook>>>

    @GET("xbook/getChapters")
    fun chapterList(@Query("bookId") bookId: Int): LiveData<XResponse<List<XChapter>>>

    @GET("xbook/getPictures")
    fun pictureList(
        @Query("bookId") bookId: Int,
        @Query("chapterId") chapterId: Long,
        @Query("free") free: Boolean
    ): LiveData<XResponse<List<XPicture>>>
}