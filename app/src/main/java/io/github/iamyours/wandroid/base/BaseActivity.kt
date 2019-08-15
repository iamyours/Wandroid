package io.github.iamyours.wandroid.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.observer.LoadingObserver

class BaseActivity : AppCompatActivity() {
    val loadingState = MutableLiveData<Boolean>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadingState.observe(this, LoadingObserver(this))
    }
}