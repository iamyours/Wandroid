package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemArticleBinding
import io.github.iamyours.wandroid.ui.web.WebActivity
import io.github.iamyours.wandroid.vo.ArticleVO

class ArticleAdapter : DataBoundAdapter<ArticleVO, ItemArticleBinding>() {
    override fun initView(binding: ItemArticleBinding, item: ArticleVO) {
        binding.vo = item
        binding.root.setOnClickListener {
            WebActivity.nav(item.link, it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_article
}