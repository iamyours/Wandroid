package io.github.iamyours.wandroid.ui.wx

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentWxArticleBinding
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.vo.WXChapterVO

class WxArticleFragment : BaseFragment<FragmentWxArticleBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_wx_article

    val vm by viewModel<WxArticleVM>()
    val fragments = ArrayList<Fragment>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        vm.chapters.observe(this, Observer {
            Log.e("test","$it")
            initViewPager(it)
        })
        vm.loadData()
    }

    private fun initViewPager(chapters: List<WXChapterVO>) {
        val titles = arrayOfNulls<String>(chapters.size)
        chapters.forEachIndexed { index, vo ->
            titles[index] = vo.name
            fragments.add(WXArticleListFragment.create(vo.id))
        }
        binding.run {
            viewPager.adapter = VpAdapter()
            tabLayout.setViewPager(viewPager, titles)
        }
    }

    inner class VpAdapter : FragmentStatePagerAdapter(childFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

    }
}