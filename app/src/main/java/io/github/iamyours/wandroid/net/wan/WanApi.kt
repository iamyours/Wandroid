package io.github.iamyours.wandroid.net.wan

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.net.ApiFactory
import io.github.iamyours.wandroid.vo.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WanApi {
    companion object {
        fun get(): WanApi {
            return ApiFactory.create(
                "https://www.wanandroid.com/",
                true
            ) { code, msg, obj ->
                WanResponse(obj, code, msg)
            }
        }
    }

    /**
     * 首页banner
     */
    @GET("banner/json")
    fun bannerList(): LiveData<WanResponse<List<BannerVO>>>

    /**
     * 文章列表
     */
    @GET("article/list/{page}/json")
    fun articleList(
        @Path("page") page: Int
    ): LiveData<WanResponse<PageVO<ArticleVO>>>

    /**
     * 置顶文章列表
     */
    @GET("article/top/json")
    fun articleTopList(
    ): LiveData<WanResponse<List<ArticleVO>>>

    /**
     * 知识体系下文章
     */
    @GET("article/list/{page}/json")
    fun articleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): LiveData<WanResponse<PageVO<ArticleVO>>>

    /**
     * 问答列表
     */
    @GET("wenda/list/{page}/json")
    fun qAList(
        @Path("page") page: Int
    ): LiveData<WanResponse<PageVO<ArticleVO>>>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    fun projectTree(): LiveData<WanResponse<List<ProjectCategoryVO>>>

    /**
     * 项目列表
     */
    @GET("project/list/{page}/json")
    fun projectList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): LiveData<WanResponse<PageVO<ArticleVO>>>

    /**
     * 公众号分类
     */
    @GET("wxarticle/chapters/json")
    fun wxChapters(): LiveData<WanResponse<List<WXChapterVO>>>

    /**
     * 公众号文章
     */
    @GET("wxarticle/list/{id}/{page}/json")
    fun wxArticlePage(
        @Path("id") id: Int,
        @Path("page") page: Int
    ): LiveData<WanResponse<PageVO<ArticleVO>>>

    /**
     * 登录
     */
    @POST("user/login")
    fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): LiveData<WanResponse<LoginUserVO>>

    /**
     * 用户信息（积分/排名等）
     */
    @GET("lg/coin/userinfo/json")
    fun userInfo(): LiveData<WanResponse<UserInfoVO>>

    /**
     * 收藏
     */
    @POST("lg/collect/{id}/json")
    fun collect(@Path("id") articleId: Int): LiveData<WanResponse<String>>

    /**
     * 取消收藏
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun uncollect(@Path("id") articleId: Int): LiveData<WanResponse<String>>

    /**
     * 收藏文章分页列表
     */
    @GET("lg/collect/list/{page}/json")
    fun collectPage(@Path("page") page: Int): LiveData<WanResponse<PageVO<ArticleVO>>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    fun searchArticlePage(
        @Path("page") page: Int,
        @Query("k") keyword: String
    ): LiveData<WanResponse<PageVO<ArticleVO>>>
}