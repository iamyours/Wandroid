package io.github.iamyours.wandroid.adapter

import android.os.Bundle
import io.github.iamyours.router.ARouter
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXchapterBinding
import io.github.iamyours.wandroid.util.RouterUtil
import io.github.iamyours.wandroid.vo.xxmh.XChapter

class XChapterAdapter :
    DataBoundAdapter<XChapter, ItemXchapterBinding>() {
    override fun initView(binding: ItemXchapterBinding, item: XChapter) {
        binding.vo = item
        binding.root.setOnClickListener {
            val index = mData.indexOf(item)
            val bundle = Bundle()
            bundle.putInt("index", index)
            bundle.putParcelableArrayList("chapters", mData)
            ARouter.getInstance().build("/xpicture")
                .with(bundle).navigation(it.context)
        }
    }

    override val layoutId: Int
        get() = R.layout.item_xchapter
}