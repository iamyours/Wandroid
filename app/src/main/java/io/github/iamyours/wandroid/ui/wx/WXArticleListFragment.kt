package io.github.iamyours.wandroid.ui.wx

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.WxArticleAdapter
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentWxArticleListBinding
import io.github.iamyours.wandroid.extension.arg
import io.github.iamyours.wandroid.extension.viewModel

class WXArticleListFragment : BaseFragment<FragmentWxArticleListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_wx_article_list
    val vm by viewModel<WxArticleListVM> { id = arguments?.getInt("id") ?: 0 }
    val adapter = WxArticleAdapter()

    companion object {
        fun create(id: Int): Fragment {
            return WXArticleListFragment()
                .apply {
                    arguments = Bundle().apply { putInt("id", id) }
                }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        initRecyclerView()
        vm.autoRefresh()
    }

    private fun initRecyclerView() {
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
        }
        vm.articlePage.observe(this, Observer {
            adapter.addAll(it.datas, it.curPage == 1)
        })
    }
}