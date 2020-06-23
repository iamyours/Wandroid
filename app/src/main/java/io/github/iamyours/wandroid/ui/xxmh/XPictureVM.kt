package io.github.iamyours.wandroid.ui.xxmh

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.xxmh.XBookApi
import io.github.iamyours.wandroid.util.AbsentLiveData
import io.github.iamyours.wandroid.vo.xxmh.XBook
import io.github.iamyours.wandroid.vo.xxmh.XChapter
import io.github.iamyours.wandroid.vo.xxmh.XPicture
import java.lang.RuntimeException

class XPictureVM : BaseViewModel() {
    private val xApi = XBookApi.get()
    val chapter = MutableLiveData<XChapter>()
    val drawOpen = MutableLiveData<Boolean>()
    val book = MutableLiveData<XBook>()
    val chapters = MutableLiveData<List<XChapter>>()
    val chapterSequence = MutableLiveData<Int>()

    val chapterId = MutableLiveData<Long>()

    private val _pictures = Transformations.switchMap(chapterSequence) {
        val c = getChapterWithSequence(it)
        if (c.pictureList == null)
            xApi.pictureList(c.bookId, c.id, c.freeFlag)
        else {//当已经加载过时，更加次id跳转
            chapterId.value = c.id
            chapter.value = c
            AbsentLiveData.create()
        }
    }

    val list = Transformations.map(_pictures) {
        loading.value = false
        moreLoading.value = false
        it.content.forEach { p ->
            p.url = p.url.replace(".jpg", ".html")
        }
        if (it.content.isNotEmpty()) {
            notLoaded = false
            val cId = it.content[0].chapterId
            hasMore.value = hasMoreChapter(cId)
            val currentChapter = getChapter(cId)
            currentChapter.pictureList = it.content
            chapterId.value = cId
            chapter.value = currentChapter
        }
        true
    }

    private fun hasMoreChapter(chapterId: Long): Boolean {
        val chapterCount = book.value?.chapterCount ?: 0
        return getChapter(chapterId).sequence < chapterCount
    }

    private fun getChapter(chapterId: Long): XChapter {
        chapters.value?.forEach {
            if (chapterId == it.id) {
                return it
            }
        }
        throw RuntimeException("chapter not found")
    }

    private fun getChapterWithSequence(chapterSequence: Int): XChapter {
        chapters.value?.forEach {
            if (chapterSequence == it.sequence) {
                return it
            }
        }
        throw RuntimeException("chapter not found")
    }


    fun openDrawer() {
        drawOpen.value = true
    }

    fun closeDrawer() {
        drawOpen.value = false
    }

    override fun loadMore() {
        super.loadMore()
        val old = chapterSequence.value ?: 1
        chapterSequence.value = old + 1
    }

    private var notLoaded = true
    fun loadData(sequence: Int) {
        if (notLoaded)
            loading.value = true
        chapterSequence.value = sequence
    }

    fun changeFirstPicture(pic: Any) {
        if (pic is XPicture)
            chapter.value = getChapter(pic.chapterId)
    }
}