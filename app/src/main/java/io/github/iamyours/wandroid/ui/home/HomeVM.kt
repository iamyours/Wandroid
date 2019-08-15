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

    val banners: LiveData<List<BannerVO>> = Transformations.map(bannerList) {
        loading.value = false
        it.data ?: ArrayList()
    }

    fun loadData() {
        refreshTrigger.value = true
        loading.value = true
    }
}