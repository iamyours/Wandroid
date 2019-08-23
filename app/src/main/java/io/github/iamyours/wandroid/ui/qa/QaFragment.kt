package io.github.iamyours.wandroid.ui.qa

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.QaAdapter
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentQaBinding
import io.github.iamyours.wandroid.extension.viewModel

/**
 * 问答
 */
class QaFragment : BaseFragment<FragmentQaBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_qa
    private val adapter = QaAdapter()
    val vm by viewModel<QaVM>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        binding.vm = vm
        binding.executePendingBindings()
        binding.refreshLayout.autoRefresh()
    }

    private fun initRecyclerView() {
        binding.recyclerView.also {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
        }
        vm.articlePage.observe(this, Observer {
            adapter.addAll(it.datas, it.curPage == 1)
        })
    }
}