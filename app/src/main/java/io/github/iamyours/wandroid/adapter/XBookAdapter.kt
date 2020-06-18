package io.github.iamyours.wandroid.adapter

import io.github.iamyours.router.ARouter
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXbookBinding
import io.github.iamyours.wandroid.vo.xxmh.XBook

class XBookAdapter :
    DataBoundAdapter<XBook, ItemXbookBinding>() {
    override fun initView(binding: ItemXbookBinding, item: XBook) {
        binding.vo = item
        binding.root.setOnClickListener {
            ARouter.getInstance().build("/xbookDetail")
                .withParcelable("book", item).navigation(it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_xbook
}