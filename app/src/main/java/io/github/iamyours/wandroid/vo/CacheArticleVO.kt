package io.github.iamyours.wandroid.vo

import androidx.room.Entity
import io.github.iamyours.wandroid.R

/**
 * 缓存文章列表
 */
@Entity(primaryKeys = ["link"])
data class CacheArticleVO(
    var link: String,
    var title: String,
    var timestamp: Long
){
    fun getFixedTitle(): String {//空出5个空格，用于显示logo
        return "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;$title"
    }

    fun getIcon(): Int {
        return when {
            link.contains("wanandroid.com") -> R.drawable.ic_logo_wan
            link.contains("www.jianshu.com") -> R.drawable.ic_logo_jianshu
            link.contains("juejin.im") -> R.drawable.ic_logo_juejin
            link.contains("blog.csdn.net") -> R.drawable.ic_logo_csdn
            link.contains("weixin.qq.com") -> R.drawable.ic_logo_wxi
            else -> R.drawable.ic_logo_other
        }
    }
}