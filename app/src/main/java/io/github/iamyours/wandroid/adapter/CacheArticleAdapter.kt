package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemCacheArticleBinding
import io.github.iamyours.wandroid.databinding.ItemHistoryArticleBinding
import io.github.iamyours.wandroid.util.RouterUtil
import io.github.iamyours.wandroid.vo.CacheArticleVO

class CacheArticleAdapter :
    DataBoundAdapter<CacheArticleVO, ItemCacheArticleBinding>() {
    override fun initView(binding: ItemCacheArticleBinding, item: CacheArticleVO) {
        binding.vo = item
        binding.root.setOnClickListener {
            RouterUtil.navWeb(item, it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_cache_article
}