package io.github.iamyours.wandroid.ui.cache

import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.util.SP

class CacheVM : BaseViewModel() {
    val username = SP.getString(SP.KEY_USER_NAME)
    val size = 20
    val list = Transformations.switchMap(page) {
        val offset = it * size
        AppDataBase.get().cacheDao().list(offset, size)
    }

    init {
        list.observeForever {
            refreshing.value = false
            moreLoading.value = false
            hasMore.value = it.size == size
        }
    }

    fun isFirst(): Boolean {
        return page.value == 0
    }
}