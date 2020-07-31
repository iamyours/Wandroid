package io.github.iamyours.wandroid.ui.juejin

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.net.juejin.JuejinApi

class JuejinArticleVM : BaseViewModel() {
    val before = MutableLiveData<String>()
    var lastItemTime = ""
    private val mApi = JuejinApi.get()
    private val _articleList = Transformations.switchMap(before) {
        mApi.aritcleList(it)
    }
    val articleList = Transformations.map(_articleList) {
        it?.d?.entrylist?.findLast { true }?.run { lastItemTime = createdAt }
        refreshing.value = false
        moreLoading.value = false
        if (TextUtils.isEmpty(before.value)) it?.d?.first = true
        it?.d
    }

    override fun refresh() {
        before.value = ""
        refreshing.value = true
    }

    override fun loadMore() {
        before.value = lastItemTime
        moreLoading.value = true
    }
}