package io.github.iamyours.wandroid.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentHomeBinding
import io.github.iamyours.wandroid.ui.article.ArticleFragment
import io.github.iamyours.wandroid.ui.qa.QaFragment

class HomeFragment :
    BaseFragment<FragmentHomeBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_home
    private val titles = arrayOf("热门", "每日一问")
    val fragments = arrayOf(ArticleFragment(), QaFragment())
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        binding.run {
            viewPager.adapter = VpAdapter()
            viewPager.offscreenPageLimit = fragments.size
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