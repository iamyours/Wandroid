package io.github.iamyours.wandroid.net.juejin

import androidx.lifecycle.LiveData
import io.github.iamyours.wandroid.net.ApiFactory
import io.github.iamyours.wandroid.vo.juejin.JuejinArticleVO
import retrofit2.http.GET
import retrofit2.http.Query

interface JuejinApi {
    /**
     * Android文章列表
     */
    @GET("get_entry_by_timeline?limit=20&category=5562b410e4b00c57d9b94a92&src=android")
    fun aritcleList(
        @Query("before") before: String
    ): LiveData<JuejinResponse<JuejinEntryList<JuejinArticleVO>>>

    companion object {
        fun create(): JuejinApi {
            return create("https://timeline-merger-ms.juejin.im/v1/")
        }

        fun create(url: String): JuejinApi {
            return ApiFactory.create(
                url,
                false
            ) { code, msg, obj ->
                JuejinResponse(code, msg, obj)
            }
        }
    }
}