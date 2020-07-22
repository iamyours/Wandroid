package io.github.iamyours.wandroid.adapter

import androidx.recyclerview.widget.DiffUtil
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.databinding.ItemXpictureBinding
import io.github.iamyours.wandroid.extension.logE
import io.github.iamyours.wandroid.vo.xxmh.XChapter
import io.github.iamyours.wandroid.vo.xxmh.XPicture

class XPictureAdapter :
    DataBoundAdapter<XPicture, ItemXpictureBinding>() {
    override fun initView(binding: ItemXpictureBinding, item: XPicture) {
        binding.vo = item
    }

    override val layoutId: Int
        get() = R.layout.item_xpicture


    /**
     * 展示连续章节
     */
    fun update(list: List<XChapter>, chapterSequence: Int) {
        val validList = findSequencePicture(list, chapterSequence)
        //todo java.lang.IndexOutOfBoundsException: areItemsTheSame未解决
        val diffResult = DiffUtil.calculateDiff(DiffCallback(mData, validList))
        mData.clear()
        mData.addAll(validList)
        notifyDataSetChanged()
    }

    /**
     * 找到连续章节图片列表
     */
    private fun findSequencePicture(
        list: List<XChapter>,
        chapterSequence: Int
    ): List<XPicture> {
        val array = ArrayList<XPicture>()
        val len = list.size
        //after
        for (s in chapterSequence .. len) {
            val list = getChapterWithSequence(list, s)?.pictureList ?: break
            array.addAll(list)
        }
        //before
        for (s in chapterSequence - 1 downTo 1) {
            val list = getChapterWithSequence(list, s)?.pictureList ?: break
            array.addAll(0, list)
        }
        return array
    }

    private fun getChapterWithSequence(
        list: List<XChapter>,
        sequence: Int
    ): XChapter? {
        list.forEach {
            if (it.sequence == sequence) return it
        }
        return null
    }


    inner class DiffCallback(
        private val oldData: List<XPicture>,
        private val newData: List<XPicture>
    ) : DiffUtil.Callback() {

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            "$oldItemPosition,$newItemPosition".logE()
            return oldData[oldItemPosition].id == newData[oldItemPosition].id
        }

        override fun getOldListSize(): Int {
            return oldData.size
        }

        override fun getNewListSize(): Int {
            return newData.size
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {
            return oldData[oldItemPosition].id == newData[oldItemPosition].id
        }
    }


}