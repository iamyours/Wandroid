package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemArticleBinding
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.util.LiveDataBus
import io.github.iamyours.wandroid.util.RouterUtil
import io.github.iamyours.wandroid.util.SP
import io.github.iamyours.wandroid.vo.ArticleVO
import io.github.iamyours.wandroid.vo.HistoryArticleVO

class ArticleAdapter : DataBoundAdapter<ArticleVO, ItemArticleBinding>() {
    private val dao = AppDataBase.get().historyDao()
    var username = SP.getString(SP.KEY_USER_NAME)

    init {//监听登录变化
        LiveDataBus.username.observeForever {
            username = it
            mData.forEach { a ->
                a.read = dao.isRead(it, a.id)
            }
            notifyDataSetChanged()
        }
    }

    override fun initView(binding: ItemArticleBinding, item: ArticleVO) {
        binding.vo = item
        binding.root.setOnClickListener {
            val history =
                HistoryArticleVO(item.id, username, System.currentTimeMillis())
            dao.addArticle(item)
            dao.addHistory(history)
            item.read = true
            notifyDataSetChanged()
            RouterUtil.navWeb2(item, it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_article
}