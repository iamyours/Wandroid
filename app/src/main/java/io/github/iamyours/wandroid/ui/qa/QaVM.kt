package io.github.iamyours.wandroid.ui.qa

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel

class QaVM : BaseViewModel() {
    //问答列表
    private val result = Transformations.switchMap(page) {
        api.articleList(it, 440)
    }

    val articlePage = mapPage(result)
}