package io.github.iamyours.wandroid.vo

/**
 * 项目分类
 */
data class ProjectCategoryVO(
    var courseId: Int,
    var id: Int,
    var name: String,
    var order: Int,
    var parentChapterId: Int,
    var userControlSetTop: Boolean,
    var visible: Int,
    var icon: Int,
    var select: Boolean
)