package io.github.iamyours.wandroid.vo.juejin

import android.text.TextUtils

data class JuejinArticleVO(
    val collectionCount: Int,
    val userRankIndex: Double,
    val buildTime: Double,
    val commentsCount: Int,
    val gfw: Boolean,
    val objectId: Boolean,
    val checkStatus: Boolean,
    val tags: List<JuejinTag>,
    val updatedAt: String,
    val rankIndex: Double,
    val hot: Boolean,
    val autoPass: Boolean,
    val originalUrl: String,
    val verifyCreatedAt: String,
    val createdAt: String,
    val user: JuejinUser,
    val screenshot: String?,
    val original: Boolean,
    val hotIndex: Double,
    val content: String,
    val title: String,
    val lastCommentTime: String,
    val type: String,
    val category: JuejinCategory,
    val viewsCount: Int,
    val summaryInfo: String
) {
    fun getTagText(): String {
        if (tags.isEmpty()) return category.name
        return "${category.name}/${tags[0].title}"
    }

    fun isEmptyScreen(): Boolean {
        return TextUtils.isEmpty(screenshot)
    }

    fun getCollectionCountText(): String {
        if (collectionCount == 0) return ""
        if (collectionCount > 10000) return String.format(
            "%.1fw",
            collectionCount / 1000.0f
        )
        return "$collectionCount"
    }

    fun getCommentCountText(): String {
        if (commentsCount == 0) return "评论"
        return "$commentsCount"
    }
}