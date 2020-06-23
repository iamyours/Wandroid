package io.github.iamyours.wandroid.adapter

import android.os.Bundle
import io.github.iamyours.router.ARouter
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXchapterBinding
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.util.RouterUtil
import io.github.iamyours.wandroid.vo.xxmh.XBook
import io.github.iamyours.wandroid.vo.xxmh.XChapter

class XChapterAdapter :
    DataBoundAdapter<XChapter, ItemXchapterBinding>() {
    private val dao = AppDataBase.get().bookDao()
    var book: XBook? = null
        set(value) {
            value?.let {
                chapterIndex = dao.findChapterIndex(it.id)
                field = it
            }
        }

    var chapterIndex = 0
    override fun initView(binding: ItemXchapterBinding, item: XChapter) {
        binding.chapterIndex = chapterIndex
        binding.vo = item
        binding.root.setOnClickListener {
            val bundle = Bundle()
            bundle.putInt("index", item.sequence)
            bundle.putParcelableArrayList("chapters", mData)
            book?.let { book ->
                bundle.putParcelable("book", book)
                dao.insertBook(book)
            }
            ARouter.getInstance().build("/xpicture")
                .with(bundle).navigation(it.context) { _, _, data ->
                    chapterIndex = data?.getIntExtra("chapterIndex", 0) ?: 0
                    notifyDataSetChanged()
                }
        }
    }

    override val layoutId: Int
        get() = R.layout.item_xchapter
}