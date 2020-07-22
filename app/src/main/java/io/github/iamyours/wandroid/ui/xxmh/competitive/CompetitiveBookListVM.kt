package io.github.iamyours.wandroid.ui.xxmh.competitive

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.xxmh.XBookApi

class CompetitiveBookListVM : BaseViewModel() {
    val xApi = XBookApi.get("http://xxmh106.com/")
    private val _bookPage = Transformations.switchMap(page) {
        xApi.competitivePage(it)
    }

    val bookPage = Transformations.map(_bookPage) {
        it.content?.run {
            if (pageNum == 1) {
                refreshing.value = false
            } else {
                moreLoading.value = false
            }
            hasMore.value = hasNextPage
        }
        it.content
    }

    override fun refresh() {
        page.value = 1
        refreshing.value = true
    }

}