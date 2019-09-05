package io.github.iamyours.wandroid.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.util.AbsentLiveData
import io.github.iamyours.wandroid.util.SP
import io.github.iamyours.wandroid.vo.BannerVO

class ArticleVM : BaseViewModel() {
    val dao = AppDataBase.get().historyDao()
    val username = SP.getString(SP.KEY_USER_NAME)
    private val articleList = Transformations.switchMap(page) {
        api.articleList(it)
    }

    val _articlePage = mapPage(articleList)

    val articlePage = Transformations.map(_articlePage) {
        it.datas.forEach { a ->
            a.read = dao.isRead(username, a.id)
        }
        it
    }

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