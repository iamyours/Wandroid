package io.github.iamyours.wandroid.net.xxmh

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.App
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.net.ApiFactory
import io.github.iamyours.wandroid.net.CacheInterceptor
import io.github.iamyours.wandroid.net.LiveDataCallAdapterFactory
import io.github.iamyours.wandroid.net.wan.WanResponse
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
            return ApiFactory.create(url, false) { code, msg, content ->
                XResponse(msg, code, content)
            }
        }
    }

    @GET("xbook/bookPage?size=20")
    fun bookPage(
        @Query("page") page: Int,
        @Query("title") title: String
    ): LiveData<XResponse<XPage<XBook>>>

    @GET("xbook/getChapters")
    fun chapterList(@Query("bookId") bookId: Int): LiveData<XResponse<List<XChapter>>>

    @GET("xbook/getPictures")
    fun pictureList(
        @Query("bookId") bookId: Int,
        @Query("chapterId") chapterId: Long,
        @Query("free") free: Boolean
    ): LiveData<XResponse<List<XPicture>>>

    @GET("home/query/books?filter=competitive&type=cartoon&paged=true&size=20")
    fun competitivePage(
        @Query("page") page: Int
    ): LiveData<XResponse<XPage<XBook>>>

    @GET("home/query/books?type=cartoon&paged=true&size=20")
    fun updateBookPage(
        @Query("page") page: Int,
        @Query("updateDay") day: Int
    ): LiveData<XResponse<XPage<XBook>>>
}