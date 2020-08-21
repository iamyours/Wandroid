package io.github.iamyours.wandroid.ui.xxmh.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityUpdateXbookBinding
import java.util.*
import kotlin.collections.ArrayList

@Route(path = "/updateBook")
class UpdateXBookActivity : BaseActivity<ActivityUpdateXbookBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_update_xbook
    private val titles =
        arrayOf("星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
    val fragments = ArrayList<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTabLayout()
    }

    private fun initTabLayout() {
        binding.tabLayout.run {
            titles.forEachIndexed { index, _ ->
                val fragment = XBookListFragment.newInstance(index)
                fragments.add(fragment)
            }
            binding.viewPager.adapter = VpAdapter()
            setViewPager(binding.viewPager, titles)
        }
        val today = getToday()
        binding.tabLayout.setCurrentTab(today, true)
    }

    private fun getToday(): Int {
        val c = Calendar.getInstance()
        return c.get(Calendar.DAY_OF_WEEK) - 1
    }

    inner class VpAdapter : FragmentStatePagerAdapter(supportFragmentManager) {
        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }
    }
}