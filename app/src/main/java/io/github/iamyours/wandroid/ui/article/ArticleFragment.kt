package io.github.iamyours.wandroid.ui.article

import android.os.Bundle
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import cn.bingoogolapple.bgabanner.BGABanner
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.ArticleAdapter
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentArticleBinding
import io.github.iamyours.wandroid.extension.displayWithUrl
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.ui.web.WebActivity
import io.github.iamyours.wandroid.vo.BannerVO

/**
 * 文章列表
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_article
    val vm by viewModel<ArticleVM>()
    private val adapter = ArticleAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initRecyclerView()
        initBanner()
        binding.vm = vm
        binding.executePendingBindings()
        binding.refreshLayout.autoRefresh()

    }

    private fun initBanner() {
        binding.banner.run {
            val adapter: BGABanner.Adapter<ImageView, BannerVO> =
                BGABanner.Adapter { _, image,
                                    model,
                                    _ ->
                    image.displayWithUrl(model?.imagePath)
                }
            setAdapter(adapter)
            setDelegate { _, _, model, _ ->
                if (model is BannerVO) {
                    WebActivity.nav(model.url, activity!!)
                }
            }
        }
//        vm.banners.observe(this, Observer {//改用databinding
//            binding.banner.setData(it, null)
//        })
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