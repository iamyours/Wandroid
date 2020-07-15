package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.JuejinItemArticleBinding
import io.github.iamyours.wandroid.util.RouterUtil
import io.github.iamyours.wandroid.vo.juejin.JuejinArticleVO

class JuejinArticleAdapter :
    DataBoundAdapter<JuejinArticleVO, JuejinItemArticleBinding>() {
    override val layoutId: Int
        get() = R.layout.juejin_item_article

    override fun initView(
        binding: JuejinItemArticleBinding,
        item: JuejinArticleVO
    ) {
        binding.vo = item
        binding.root.setOnClickListener {
            RouterUtil.navWeb2(item.originalUrl, it.context)
        }
    }
}