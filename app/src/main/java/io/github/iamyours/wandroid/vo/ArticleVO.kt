package io.github.iamyours.wandroid.vo

import io.github.iamyours.wandroid.R

data class ArticleVO(
    var id: Int,
    var author: String,
    var chapterId: Int,
    var chapterName: String,
    var collect: Boolean,
    var fresh: Boolean,
    var courseId: Int,
    var desc: String,
    var link: String,
    var niceDate: String,
    var publishTime: Long,
    var superChapterId: Int,
    var superChapterName: String,
    var title: String
) {

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