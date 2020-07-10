package io.github.iamyours.wandroid.vo.juejin

data class JuejinUser(
    val collectedEntriesCount: Int,
    val company: String,
    val followersCount: Long,
    val followeesCount: Long,
    val role: String,
    val postedPostsCount: Int,
    val level: Int,
    val isAuthor: Boolean,
    val postedEntriesCount: Int,
    val totalCommentsCount: Int,
    val viewedEntriesCount: Long,
    val jobTitle: String,
    val username: String,
    val avatarLarge: String,
    val objectId: String
)