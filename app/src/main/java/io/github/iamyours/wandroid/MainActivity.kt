package io.github.iamyours.wandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import io.github.iamyours.wandroid.databinding.ActivityMainBinding
import io.github.iamyours.wandroid.ui.home.HomeFragment
import io.github.iamyours.wandroid.ui.mine.MineFragment
import io.github.iamyours.wandroid.ui.project.ProjectFragment
import io.github.iamyours.wandroid.ui.wx.WxArticleFragment
import io.github.iamyours.wandroid.vo.TabItem
import kotlinx.android.synthetic.main.view_tab.view.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private val tabs = arrayOf(
        TabItem(R.drawable.tab_home, "首页", HomeFragment::class.java),
        TabItem(R.drawable.tab_project, "项目", ProjectFragment::class.java),
        TabItem(R.drawable.tab_wx, "公众号", WxArticleFragment::class.java),
        TabItem(R.drawable.tab_mine, "我", MineFragment::class.java)
    )
    private val fragments = ArrayList<Fragment>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initFragments()
        initTabLayout()
    }

    /**
     * fragment重叠
     */
    override fun onSaveInstanceState(outState: Bundle) {
        fragments.forEach {
            supportFragmentManager.putFragment(
                outState,
                it.javaClass.simpleName,
                it
            )
        }
        super.onSaveInstanceState(outState)
    }

    private fun initFragments() {
        if (fragments.isEmpty()) {
            tabs.forEach {
                val f = it.fragmentCls.newInstance()
                fragments.add(f)
            }
        }
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEach {
            if (!it.isAdded) transaction.add(
                R.id.fl_content, it, it.javaClass
                    .simpleName
            ).hide(it)
        }
        transaction.commit()
    }

    private fun initTabLayout() {
        binding.tabLayout.run {
            tabs.forEach {
                addTab(newTab().setCustomView(getTabView(it)))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabReselected(p0: TabLayout.Tab?) {}

                override fun onTabUnselected(p0: TabLayout.Tab?) {}

                override fun onTabSelected(p0: TabLayout.Tab) {
                    initTab(p0.position)
                }
            })
            getTabAt(0)?.select()
        }
        initTab(0)
    }

    private fun getTabView(it: TabItem): View {
        val v = LayoutInflater.from(this).inflate(R.layout.view_tab, null)
        v.tab_icon.setImageResource(it.icon)
        v.tab_name.text = it.name
        return v
    }

    private fun initTab(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        fragments.forEachIndexed { index, fragment ->
            if (index == position) {
                transaction.show(fragment)
            } else {
                transaction.hide(fragment)
            }
        }
        transaction.commit()
    }

}
