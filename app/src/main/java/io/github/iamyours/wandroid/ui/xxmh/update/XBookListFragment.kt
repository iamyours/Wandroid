package io.github.iamyours.wandroid.ui.xxmh.update

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.XCompetitiveBookAdapter
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentXbookListBinding
import io.github.iamyours.wandroid.extension.arg
import io.github.iamyours.wandroid.extension.viewModel

class XBookListFragment : BaseFragment<FragmentXbookListBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_xbook_list
    val vm by viewModel<XBookListVM>()
    val adapter = XCompetitiveBookAdapter()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = vm
        vm.day = arguments?.getInt("day")?:0
        binding.executePendingBindings()
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(activity)
        }
        vm.bookPage.observe(this, Observer {
            it?.run {
                adapter.addAll(list, pageNum == 1)
            }
        })
        vm.autoRefresh()
    }

    companion object {
        fun newInstance(day: Int): Fragment {
            val fragment = XBookListFragment()
            val arg = Bundle()
            arg.putInt("day", day)
            fragment.arguments = arg
            return fragment
        }
    }
}