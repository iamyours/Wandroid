package io.github.iamyours.wandroid.vo

data class LoginUserVO(
    var admin: Boolean,
    var chapterTops: List<String>,
    var collectIds: List<Int>,
    var email: String,
    var icon: String,
    var nickname: String,
    var username: String
)