package io.github.iamyours.wandroid.ui.xxmh

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.XChapterAdapter
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityXbookDetailBinding
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.vo.xxmh.XBook

@Route(path = "/xbookDetail")
class XBookDetailActivity : BaseActivity<ActivityXbookDetailBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_xbook_detail
    val vm by viewModel<XBookDetailVM>()
    val adapter = XChapterAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        vm.book.value = intent.getParcelableExtra("book")
        initRecyclerView()
        vm.loadData()
    }

    private fun initRecyclerView() {
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        }
        vm.list.observe(this, Observer {
            adapter.addAll(it, true)
        })
    }
}