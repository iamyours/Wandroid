package io.github.iamyours.wandroid.adapter

import io.github.iamyours.router.ARouter
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXcompetitiveBookBinding
import io.github.iamyours.wandroid.vo.xxmh.XBook

class XCompetitiveBookAdapter :
    DataBoundAdapter<XBook, ItemXcompetitiveBookBinding>() {
    override fun initView(binding: ItemXcompetitiveBookBinding, item: XBook) {
        binding.vo = item
        binding.root.setOnClickListener {
            ARouter.getInstance().build("/xbookDetail")
                .withParcelable("book", item).navigation(it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_xcompetitive_book
}