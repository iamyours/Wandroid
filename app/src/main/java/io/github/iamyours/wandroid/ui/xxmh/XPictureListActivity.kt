package io.github.iamyours.wandroid.ui.xxmh

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.XMenuChapterAdapter
import io.github.iamyours.wandroid.adapter.XPictureAdapter
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityXpictureListBinding
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.extension.logE
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.vo.xxmh.XBook
import io.github.iamyours.wandroid.vo.xxmh.XChapter

@Route(path = "/xpicture")
class XPictureListActivity : BaseActivity<ActivityXpictureListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_xpicture_list
    val vm by viewModel<XPictureVM>()
    val adapter = XPictureAdapter()
    private val chapterAdapter = XMenuChapterAdapter()
    private val dao = AppDataBase.get().bookDao()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        val chapters = intent.getParcelableArrayListExtra<XChapter>("chapters")
        val index = intent.getIntExtra("index", 1)
        val book = intent.getParcelableExtra<XBook>("book")
        vm.book.value = book
        vm.chapters.value = chapters
        initRecyclerView()
        chapterAdapter.chapterIndex = index
        chapterAdapter.addAll(chapters, true)
        vm.loadData(index)
        vm.chapter.observe(this, Observer {
            chapterAdapter.chapterIndex = it.sequence
        })
        vm.chapterId.observe(this, Observer {
            scrollToChapter(it)
        })
        chapterAdapter.itemClick = {
            vm.loadData(it)
            vm.closeDrawer()
        }
    }

    private fun scrollToChapter(chapterId: Long) {
        adapter.getData().forEachIndexed { index, p ->
            if (p.chapterId == chapterId && p.sequence == 1) {
                layoutManager.scrollToPositionWithOffset(index, 0)
                "chapterId:$chapterId,se:${p.sequence}".logE()
            }
        }
    }

    override fun onBackPressed() {
        backAction()
    }

    private fun backAction() {
        vm.chapter.value?.let {
            dao.updateBook(System.currentTimeMillis(), it.sequence, it.bookId)
            val data = Intent()
            data.putExtra("chapterIndex", it.sequence)
            setResult(100, data)
        }
        finish()
    }

    lateinit var layoutManager: LinearLayoutManager

    private fun initRecyclerView() {
        binding.recyclerView.let {
            it.adapter = adapter
            layoutManager = LinearLayoutManager(this)
            it.layoutManager = layoutManager
        }
        vm.list.observe(this, Observer {
            val chapterSequence = vm.chapterSequence.value ?: 1
            adapter.update(
                vm.chapters.value ?: ArrayList(),
                chapterSequence
            )
            val cId = vm.chapterId.value ?: 0
            scrollToChapter(cId)
        })
        binding.rvChapter.let {
            it.adapter = chapterAdapter
            it.layoutManager = LinearLayoutManager(this)
        }
    }
}