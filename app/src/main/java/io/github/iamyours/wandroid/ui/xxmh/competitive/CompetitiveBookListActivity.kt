package io.github.iamyours.wandroid.ui.xxmh.competitive

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.adapter.XCompetitiveBookAdapter
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityCompetitiveBookListBinding
import io.github.iamyours.wandroid.extension.viewModel

@Route(path = "/competitiveBook")
class CompetitiveBookListActivity :
    BaseActivity<ActivityCompetitiveBookListBinding>() {
    override val layoutId: Int
        get() = R.layout.activity_competitive_book_list
    val vm by viewModel<CompetitiveBookListVM>()
    val adapter = XCompetitiveBookAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        binding.executePendingBindings()
        binding.recyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(this)
        }
        vm.bookPage.observe(this, Observer {
            it?.run {
                adapter.addAll(list, pageNum == 1)
            }
        })
        vm.autoRefresh()
    }
}