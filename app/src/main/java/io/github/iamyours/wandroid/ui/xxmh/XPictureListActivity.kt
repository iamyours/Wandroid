package io.github.iamyours.wandroid.ui.xxmh

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.XPictureAdapter
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityXpictureListBinding
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.vo.xxmh.XChapter

@Route(path = "/xpicture")
class XPictureListActivity : BaseActivity<ActivityXpictureListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_xpicture_list
    val vm by viewModel<XPictureVM>()
    val adapter = XPictureAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        val chapters = intent.getParcelableArrayListExtra<XChapter>("chapters")
        val index = intent.getIntExtra("index", 0)
        vm.chapter.value = chapters[index]
        initRecyclerView()
        vm.loadData()
    }

    private fun initRecyclerView() {
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }
        vm.list.observe(this, Observer {
            adapter.addAll(it, false)
        })
    }
}