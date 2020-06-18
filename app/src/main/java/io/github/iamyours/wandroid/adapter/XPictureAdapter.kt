package io.github.iamyours.wandroid.adapter

import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXpictureBinding
import io.github.iamyours.wandroid.vo.xxmh.XPicture

class XPictureAdapter :
    DataBoundAdapter<XPicture, ItemXpictureBinding>() {
    override fun initView(binding: ItemXpictureBinding, item: XPicture) {
        binding.vo = item
    }

    override val layoutId: Int
        get() = R.layout.item_xpicture
}