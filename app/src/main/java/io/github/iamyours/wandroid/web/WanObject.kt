package io.github.iamyours.wandroid.web

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.observer.LoadingObserver
import io.github.iamyours.wandroid.ui.login.LoginActivity
import io.github.iamyours.wandroid.util.Constants
import io.github.iamyours.wandroid.util.FileUtil

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

    /**
     * 离线保存html
     */
    @JavascriptInterface
    fun saveHtml(url: String, html: String) {
        loading.postValue(true)
        Constants.IO.execute {
            FileUtil.saveHtml(url, html)
            loading.postValue(false)
        }
    }

    /**
     * 显示图片
     */
    @JavascriptInterface
    fun showImage(url: String) {

    }
}