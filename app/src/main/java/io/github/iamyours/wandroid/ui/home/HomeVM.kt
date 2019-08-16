package io.github.iamyours.wandroid.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.github.iamyours.wandroid.net.ApiResponse
import io.github.iamyours.wandroid.net.WanApi
import io.github.iamyours.wandroid.vo.BannerVO

class HomeVM : ViewModel() {
    private val refreshTrigger = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    private val api = WanApi.get()
    private val bannerList: LiveData<ApiResponse<List<BannerVO>>> = Transformations.switchMap(refreshTrigger) {
        //当refreshTrigger的值被设置时，bannerList
        api.bannerList()
    }

    private val page = MutableLiveData<Int>()
    val refreshing = MutableLiveData<Boolean>()
    val moreLoading = MutableLiveData<Boolean>()
    val hasMore = MutableLiveData<Boolean>()
    private val articleList = Transformations.switchMap(page) {
        api.articleList(it)
    }

    val articlePage = Transformations.map(articleList) {
        refreshing.value = false
        moreLoading.value = false
        hasMore.value = !(it?.data?.over ?: false)
        it.data
    }

    fun loadMore() {
        page.value = (page.value ?: 0) + 1
        moreLoading.value = true
    }

    fun refresh() {
        loadBanner()
        page.value = 0
        refreshing.value = true
    }

    val banners: LiveData<List<BannerVO>> = Transformations.map(bannerList) {
        loading.value = false
        it.data ?: ArrayList()
    }

    fun loadBanner() {
        refreshTrigger.value = true
        loading.value = true
    }
}