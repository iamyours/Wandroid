package io.github.iamyours.wandroid.vo

data class UserInfoVO(
    var coinCount: Int,
    var rank: Int,
    var userId: Int,
    var username: String
) {
    fun getLevel(): String {
        return "lv${1 + coinCount / 100}"
    }

    fun getCoinText(): String {
        return "$coinCount"
    }

    fun getRankText(): String {
        return "排名 $rank"
    }
}