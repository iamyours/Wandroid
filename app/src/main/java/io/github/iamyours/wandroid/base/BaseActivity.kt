package io.github.iamyours.wandroid.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.observer.LoadingObserver

open abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    abstract val layoutId: Int
    lateinit var binding: T
    val loadingState = MutableLiveData<Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }catch (e:Exception){}
        binding = DataBindingUtil.setContentView(this, layoutId)
        binding.lifecycleOwner = this
        loadingState.observe(this, LoadingObserver(this))
    }
}