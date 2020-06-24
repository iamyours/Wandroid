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
        val isLast = it?.content?.isLastPage ?: true
        hasMore.value = !isLast
        it?.content?.list?.forEach { b ->
            b.coverUrl = b.coverUrl.replace(".jpg", ".html")
            b.extensionUrl = b.extensionUrl.replace(".jpg", ".html")
        }
        it?.content?.list ?: ArrayList()
    }

    override fun refresh() {
        page.value = 1
        refreshing.value = true
    }

    fun isFirst(): Boolean {
        return page.value == 1
    }
}