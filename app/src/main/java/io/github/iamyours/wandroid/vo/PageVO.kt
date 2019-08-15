package io.github.iamyours.wandroid.vo

data class PageVO<T>(
    var curPage: Int,
    var datas: List<T>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)