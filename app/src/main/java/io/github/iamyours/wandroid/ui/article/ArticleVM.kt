package io.github.iamyours.wandroid.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.util.AbsentLiveData
import io.github.iamyours.wandroid.vo.BannerVO

class ArticleVM : BaseViewModel() {

    private val articleList = Transformations.switchMap(page) {
        api.articleList(it)
    }

    val articlePage = mapPage(articleList)

    private val bannerList = Transformations.switchMap(page) {
        if (it == 0)
            api.bannerList()
        else
            AbsentLiveData.create()
    }
    val banners: LiveData<List<BannerVO>> = Transformations.map(bannerList) {
        loading.value = false
        it.data ?: ArrayList()
    }


}