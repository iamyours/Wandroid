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
    val errorMsg = MutableLiveData<String>()


    val image = MutableLiveData<PositionImage>()

    /**
     * 收藏接口
     */
    val _collectAction = Transformations.switchMap(refreshTrigger) {
        val id = articleId.value ?: 0
        if (it) {
            if (id > 0)
                api.collect(id)
            else
                api.collect(title.value, articleUrl.value)
        } else {
            api.uncollect(id)
        }
    }

    private val collectAction = Transformations.map(_collectAction) {
        loading.value = false
        if (it.errorCode == 0) {
            collect.value = !(collect.value ?: false)
            it.data?.run {
                articleId.value = id
            }
        } else {
            errorMsg.value = it.errorMsg
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