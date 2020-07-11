package io.github.iamyours.wandroid.ui.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.util.JsoupUtil
import io.github.iamyours.wandroid.web.PositionImage

class Web2VM : BaseViewModel() {
    val loaded = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
    val collect = MutableLiveData<Boolean>()
    val articleId = MutableLiveData<Int>()
    val showMore = MutableLiveData<Boolean>()
    val articleUrl = MutableLiveData<String>()


    val image = MutableLiveData<PositionImage>()

    val html = Transformations.switchMap(articleUrl) {
        JsoupUtil.parseHtml(it)
    }

    /**
     * 收藏接口
     */
    val _collectAction = Transformations.switchMap(refreshTrigger) {
        val id = articleId.value ?: 0
        if (it) {
            api.collect(id)
        } else {
            api.uncollect(id)
        }
    }

    val collectAction = Transformations.map(_collectAction) {
        loading.value = false
        if (it.errorCode == 0) {
            collect.value = !(collect.value ?: false)
        }
    }


    init {
        collectAction.observeForever { }
    }

    fun collectOrNot() {
        if (isNotLogin()) return
        val state = !(collect.value ?: false)
        refreshTrigger.value = state
        loading.value = true
    }

    fun showMore() {
        showMore.value = true
    }

}