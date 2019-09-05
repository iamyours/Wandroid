package io.github.iamyours.wandroid.ui.history

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.QaAdapter
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityHistoryArticleListBinding
import io.github.iamyours.wandroid.extension.viewModel

@Route(path = "/history")
class HistoryArticleListActivity :
    BaseActivity<ActivityHistoryArticleListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_history_article_list
    val vm by viewModel<HistoryVM>()
    val adapter = QaAdapter()

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
        vm.list.observe(this, Observer {
            adapter.addAll(it, vm.isFirst())
        })
    }
}