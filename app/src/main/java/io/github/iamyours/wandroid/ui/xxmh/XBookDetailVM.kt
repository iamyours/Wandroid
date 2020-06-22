package io.github.iamyours.wandroid.ui.xxmh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.xxmh.XBookApi
import io.github.iamyours.wandroid.vo.xxmh.XBook

class XBookDetailVM : BaseViewModel() {
    private val xApi = XBookApi.get()
    val book = MutableLiveData<XBook>()
    private val _list = Transformations.switchMap(refreshTrigger) {
        xApi.chapterList(book.value?.id ?: 0)
    }
    val list = Transformations.map(_list) {
        loading.value = false
        it.content.forEach { c ->
            c.coverUrl = c.coverUrl.replace(".jpg", ".html")
        }
        it.content
    }
}