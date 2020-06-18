package io.github.iamyours.wandroid.ui.mine

import android.os.Bundle
import androidx.lifecycle.Observer
import io.github.iamyours.router.ARouter
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseFragment
import io.github.iamyours.wandroid.databinding.FragmentMineBinding
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.util.Constants

class MineFragment : BaseFragment<FragmentMineBinding>() {
    override val layoutId: Int
        get() = R.layout.fragment_mine
    val vm by viewModel<MineVM>()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.vm = vm
        vm.toLogin.observe(this, Observer {
            loginAction()
        })
        vm.route.observe(this, Observer {
            ARouter.getInstance().build(it)
                .navigation(activity)
        })
    }

    private fun loginAction() {
        ARouter.getInstance()
            .build("/login")
            .navigation(activity) { _, resultCode, _ ->
                if (resultCode == Constants.RESULT_LOGIN) {
                    vm.isLogin.value = true
                }
            }
    }
}