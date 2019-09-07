package io.github.iamyours.wandroid.ui.search

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel

class SearchVM : BaseViewModel() {
    val keyword = MutableLiveData<String>()
    //是否为空
    val empty = Transformations.map(keyword) {
        TextUtils.isEmpty(it)
    }

    private val _articlePage = Transformations.switchMap(page) {
        api.searchArticlePage(it, keyword.value ?: "")
    }
    val articlePage = mapPage(_articlePage)

    fun search() {
        autoRefresh()
    }
}