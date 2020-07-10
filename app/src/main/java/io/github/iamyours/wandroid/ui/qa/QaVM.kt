package io.github.iamyours.wandroid.ui.qa

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.extension.logE

class QaVM : BaseViewModel() {
    //问答列表
    private val result = Transformations.switchMap(page) {
        api.qAList(it)
    }

    override fun refresh() {
        page.value = 1
        refreshing.value = true
    }

    val articlePage = mapPage(result)
}