package io.github.iamyours.wandroid.ui.article

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.net.wan.WanResponse
import io.github.iamyours.wandroid.util.AbsentLiveData
import io.github.iamyours.wandroid.util.LiveDataUtil
import io.github.iamyours.wandroid.util.SP
import io.github.iamyours.wandroid.vo.ArticleVO
import io.github.iamyours.wandroid.vo.BannerVO

class ArticleVM : BaseViewModel() {
    private val dao = AppDataBase.get().historyDao()
    val username = SP.getString(SP.KEY_USER_NAME)
    private val articleList =
        Transformations.switchMap(page) {
            api.articleList(it)
        }
    private val topArticleList =
        Transformations.switchMap(page) {
            if (it == 0) api.articleTopList()
            else {
                val data = MutableLiveData<WanResponse<List<ArticleVO>>>()
                data.value =
                    WanResponse(
                        null,
                        0,
                        ""
                    )
                data
            }
        }

    val articlePage =
        LiveDataUtil.zip2(articleList, topArticleList) { list, top ->
            list.data?.datas?.let {
                top.data?.run {
                    forEach { a -> a.top = true }
                    (it as MutableList<ArticleVO>).addAll(0, this)
                }
                it.forEach { a ->
                    a.read = dao.isRead(username, a.id)
                }
            }
            hasMore.value = !(list.data?.over ?: false)
            list.data?.run {
                if (curPage == 1) {
                    refreshing.value = false
                } else {
                    moreLoading.value = false
                }
            }
            list.data
        }


    private val bannerList = Transformations.switchMap(page) {
        if (it == 0)
            api.bannerList()
        else
            AbsentLiveData.create()
    }
    val banners: LiveData<List<BannerVO>> = Transformations.map(bannerList) {
        it.data ?: ArrayList()
    }


}