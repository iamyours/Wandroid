package io.github.iamyours.wandroid.ui.xxmh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.xxmh.XBookApi
import io.github.iamyours.wandroid.vo.xxmh.XBook

class XBookDetailVM : BaseViewModel() {
    val xApi = XBookApi.get1()
    val book = MutableLiveData<XBook>()
    private val _list = Transformations.switchMap(refreshTrigger) {
        xApi.chapterList(book.value?.id ?: 0)
    }
    val list = Transformations.map(_list) {
        refreshing.value = false
        it.content
    }
}