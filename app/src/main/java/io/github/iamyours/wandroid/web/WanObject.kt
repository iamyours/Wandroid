package io.github.iamyours.wandroid.web

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.observer.LoadingObserver
import io.github.iamyours.wandroid.ui.login.LoginActivity

class WanObject(var context: Context) {
    val loading = MutableLiveData<Boolean>()

    init {
        if (context is FragmentActivity) {
            loading.observe(
                context as FragmentActivity,
                LoadingObserver(context)
            )
        }
    }

    @JavascriptInterface
    fun showLoading() {
        loading.postValue(true)
    }

    @JavascriptInterface
    fun hideLoading() {
        loading.postValue(false)
    }

    @JavascriptInterface
    fun toLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}