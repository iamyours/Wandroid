package io.github.iamyours.wandroid.ui.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.CollectArticleAdapter
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityCollectListBinding
import io.github.iamyours.wandroid.extension.viewModel

@Route(path = "/collect")
class CollectListActivity : BaseActivity<ActivityCollectListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_collect_list
    val vm by viewModel<CollectVM>()

    val adapter = CollectArticleAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        initRecyclerView()
        vm.autoRefresh()
    }

    private fun initRecyclerView() {
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }
        vm.collectPage.observe(this, Observer {
            it.datas.forEach { a ->
                a.collect = true
                a.id = a.originId
            }
            adapter.addAll(it.datas, it.curPage == 1)
        })
    }
}