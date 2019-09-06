package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemWxArticleBinding
import io.github.iamyours.wandroid.util.RouterUtil
import io.github.iamyours.wandroid.vo.ArticleVO

class WxArticleAdapter :
    DataBoundAdapter<ArticleVO, ItemWxArticleBinding>() {
    override fun initView(binding: ItemWxArticleBinding, item: ArticleVO) {
        binding.vo = item
        binding.root.setOnClickListener {
            RouterUtil.navWeb(item,it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_wx_article
}