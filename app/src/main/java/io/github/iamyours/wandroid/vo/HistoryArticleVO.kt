package io.github.iamyours.wandroid.vo

import androidx.room.Entity

/**
 * 用户阅读历史
 */
@Entity(primaryKeys = ["articleId", "username"])
data class HistoryArticleVO(
    var articleId: Int,
    var username: String,
    var timestamp: Long
)