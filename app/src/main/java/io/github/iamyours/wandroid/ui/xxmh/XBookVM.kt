package io.github.iamyours.wandroid.ui.xxmh

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.xxmh.XBookApi

class XBookVM : BaseViewModel() {
    val xApi = XBookApi.get()
    private val bookPage = Transformations.switchMap(page) {
        xApi.bookPage(it)
    }
    val list = Transformations.map(bookPage) {
        moreLoading.value = false
        refreshing.value = false
        hasMore.value = !it.content.isLastPage
        it.content.list.forEach { b ->
            b.coverUrl = b.coverUrl.replace(".jpg", ".html")
        }
        it.content.list
    }

    override fun refresh() {
        page.value = 1
        refreshing.value = true
    }

    fun isFirst(): Boolean {
        return page.value == 1
    }
}