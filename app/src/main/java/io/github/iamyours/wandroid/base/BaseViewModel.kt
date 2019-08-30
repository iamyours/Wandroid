package io.github.iamyours.wandroid.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.iamyours.wandroid.net.ApiResponse
import io.github.iamyours.wandroid.net.WanApi
import io.github.iamyours.wandroid.vo.PageVO

open class BaseViewModel : ViewModel() {
    val refreshTrigger = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    protected val api = WanApi.get()

    protected val page = MutableLiveData<Int>()
    val refreshing = MutableLiveData<Boolean>()
    val moreLoading = MutableLiveData<Boolean>()
    val hasMore = MutableLiveData<Boolean>()
    val autoRefresh = MutableLiveData<Boolean>()

    fun loadMore() {
        page.value = (page.value ?: 0) + 1
        moreLoading.value = true
    }

    /**
     * 自动刷新
     */
    fun autoRefresh() {
        autoRefresh.value = true
    }

    open fun refresh() {
        page.value = 0
        refreshing.value = true
    }

    /**
     * 处理分页数据
     */
    fun <T> mapPage(source: LiveData<ApiResponse<PageVO<T>>>): LiveData<PageVO<T>> {
        return Transformations.map(source) {
            refreshing.value = false
            moreLoading.value = false
            hasMore.value = !(it?.data?.over ?: false)
            it.data
        }
    }

    open fun loadData() {
        refreshTrigger.value = true
        loading.value = true
    }

    /**
     * 绑定登录状态
     */
    fun attachLoading(otherState: MutableLiveData<Boolean>) {
        loading.observeForever {
            otherState.value = it
        }
    }
}