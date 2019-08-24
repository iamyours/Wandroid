package io.github.iamyours.wandroid.vo

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
    fun getMixedTitle(): String {
        return when {
            link.contains("www.wanandroid.com") -> "【玩】$title"
            link.contains("www.jianshu.com") -> "【简】$title"
            link.contains("juejin.im") -> "【掘】$title"
            link.contains("blog.csdn.net") -> "【CSDN】$title"
            link.contains("weixin.qq.com") -> "【微】$title"
            else -> title
        }
    }
}