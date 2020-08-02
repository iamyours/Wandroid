package io.github.iamyours.wandroid.ui.juejin

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.JuejinArticleAdapter
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.JuejinFragmentArticleBinding
import io.github.iamyours.wandroid.extension.viewModel

/**
 * 掘金文章列表
 */
@Deprecated(message = "废弃，掘金接口升级了")
class JuejinArticleFragment : BaseFragment<JuejinFragmentArticleBinding>() {
    override val layoutId: Int
        get() = R.layout.juejin_fragment_article
    val vm by viewModel<JuejinArticleVM>()
    val adapter = JuejinArticleAdapter()
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        binding.refreshLayout.autoRefresh()
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
        }
        vm.articleList.observe(this, Observer {
            it?.run {
                adapter.addAll(entrylist, first)
            }
        })
    }
}