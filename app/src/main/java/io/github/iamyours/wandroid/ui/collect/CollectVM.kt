package io.github.iamyours.wandroid.ui.collect

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel

class CollectVM : BaseViewModel() {
    private val _collectPage = Transformations.switchMap(page) {
        api.collectPage(it)
    }
    val collectPage = mapPage(_collectPage)
}