package io.github.iamyours.wandroid.ui.wx

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel

class WxArticleVM : BaseViewModel() {
    private val _chapters = Transformations.switchMap(refreshTrigger) {
        api.wxChapters()
    }

    val chapters = Transformations.map(_chapters) {
        loading.value = false
        it.data ?: ArrayList()
    }


}