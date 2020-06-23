package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXmenuChapterBinding
import io.github.iamyours.wandroid.databinding.ItemXpictureBinding
import io.github.iamyours.wandroid.vo.xxmh.XChapter
import io.github.iamyours.wandroid.vo.xxmh.XPicture

class XMenuChapterAdapter :
    DataBoundAdapter<XChapter, ItemXmenuChapterBinding>() {
    lateinit var itemClick: (Int) -> Unit
    var chapterIndex = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun initView(binding: ItemXmenuChapterBinding, item: XChapter) {
        binding.vo = item
        binding.chapterIndex = chapterIndex
        binding.root.setOnClickListener {
            itemClick(item.sequence)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_xmenu_chapter


}