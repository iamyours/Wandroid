package io.github.iamyours.wandroid.vo

data class ArticleVO(
    var id: Int,
    var author: String,
    var chapterId: Int,
    var chapterName: String,
    var collect: Boolean,
    var courseId: Int,
    var desc: String,
    var link: String,
    var niceDate: String,
    var publishTime: Long,
    var superChapterId: Int,
    var superChapterName: String,
    var title: String
)