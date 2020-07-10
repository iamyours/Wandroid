package io.github.iamyours.wandroid.net

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.util.SP
import io.github.iamyours.wandroid.vo.*
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface WanApi {
    companion object {
        fun get(): WanApi {
            val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)
            }
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
            return Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/")
                .client(clientBuilder.build())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WanApi::class.java)
        }
    }

    /**
     * 首页banner
     */
    @GET("banner/json")
    fun bannerList(): LiveData<ApiResponse<List<BannerVO>>>

    /**
     * 文章列表
     */
    @GET("article/list/{page}/json")
    fun articleList(
        @Path("page") page: Int
    ): LiveData<ApiResponse<PageVO<ArticleVO>>>

    /**
     * 置顶文章列表
     */
    @GET("article/top/json")
    fun articleTopList(
    ): LiveData<ApiResponse<List<ArticleVO>>>

    /**
     * 知识体系下文章
     */
    @GET("article/list/{page}/json")
    fun articleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): LiveData<ApiResponse<PageVO<ArticleVO>>>

    /**
     * 问答列表
     */
    @GET("wenda/list/{page}/json")
    fun qAList(
        @Path("page") page: Int
    ): LiveData<ApiResponse<PageVO<ArticleVO>>>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    fun projectTree(): LiveData<ApiResponse<List<ProjectCategoryVO>>>

    /**
     * 项目列表
     */
    @GET("project/list/{page}/json")
    fun projectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): LiveData<ApiResponse<PageVO<ArticleVO>>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    fun wxChapters(): LiveData<ApiResponse<List<WXChapterVO>>>

    /**
     * 公众号文章
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun wxArticlePage(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): LiveData<ApiResponse<PageVO<ArticleVO>>>

    /**
     * 登录
     */
    @POST("user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LiveData<ApiResponse<LoginUserVO>>

    /**
     * 用户信息（积分/排名等）
     */
    @GET("lg/coin/userinfo/json")
    fun userInfo(): LiveData<ApiResponse<UserInfoVO>>

    /**
     * 收藏
     */
    @POST("lg/collect/{id}/json")
    fun collect(@Path("id") articleId: Int): LiveData<ApiResponse<String>>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun uncollect(@Path("id") articleId: Int): LiveData<ApiResponse<String>>

    /**
     * 收藏文章分页列表
     */
    @GET("lg/collect/list/{page}/json")
    fun collectPage(@Path("page") page: Int): LiveData<ApiResponse<PageVO<ArticleVO>>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    fun searchArticlePage(
        @Path("page") page: Int,
        @Query("k") keyword: String
    ): LiveData<ApiResponse<PageVO<ArticleVO>>>
}