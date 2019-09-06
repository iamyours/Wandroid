package io.github.iamyours.wandroid.ui.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.iamyours.wandroid.base.BaseViewModel

class WebVM : BaseViewModel() {
    val loaded = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
    val collect = MutableLiveData<Boolean>()
    val articleId = MutableLiveData<Int>()

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
}