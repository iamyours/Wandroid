package io.github.iamyours.wandroid

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import cn.bingoogolapple.bgabanner.BGABanner
import io.github.iamyours.wandroid.databinding.ActivityMainBinding
import io.github.iamyours.wandroid.extension.displayWithUrl
import io.github.iamyours.wandroid.observer.LoadingObserver
import io.github.iamyours.wandroid.ui.home.HomeVM
import io.github.iamyours.wandroid.vo.BannerVO

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val vm = ViewModelProviders.of(this).get(HomeVM::class.java)
        binding.lifecycleOwner = this
        binding.vm = vm
        initBanner()
        vm.loading.observe(this, LoadingObserver(this))
    }

    private fun initBanner() {
        binding.run {
            val bannerAdapter = BGABanner.Adapter<ImageView, BannerVO> { _, image, model, _ ->
                image.displayWithUrl(model?.imagePath)
            }
            banner.setAdapter(bannerAdapter)
            vm?.banners?.observe(this@MainActivity, Observer {
                banner.setData(it, null)
            })
        }
    }
}
