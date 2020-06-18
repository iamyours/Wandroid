package io.github.iamyours.wandroid.ui.xxmh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.xxmh.XBookApi
import io.github.iamyours.wandroid.vo.xxmh.XChapter

class XPictureVM : BaseViewModel() {
    val xApi = XBookApi.get2()
    val chapter = MutableLiveData<XChapter>()
    private val _pictures = Transformations.switchMap(refreshTrigger) {
        val c = chapter.value
        val bookId = c?.bookId ?: 0
        val chapterId = c?.id ?: 0
        val free = c?.freeFlag ?: true
        xApi.pictureList(bookId, chapterId, free)
    }

    val list = Transformations.map(_pictures) {
        loading.value = false
        it.content
    }
}