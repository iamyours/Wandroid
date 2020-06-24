package io.github.iamyours.wandroid.web

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.github.iamyours.wandroid.observer.LoadingObserver
import io.github.iamyours.wandroid.ui.login.LoginActivity
import io.github.iamyours.wandroid.util.Constants
import io.github.iamyours.wandroid.util.FileUtil

class WanObject(
    var context: Context,
    var image: MutableLiveData<PositionImage>
) {
    val loading = MutableLiveData<Boolean>()
    val msg = MutableLiveData<String>()

    init {
        if (context is FragmentActivity) {
            val activity = context as FragmentActivity
            loading.observe(
                activity,
                LoadingObserver(context)
            )
            msg.observe(activity, Observer {
                Toast.makeText(
                    activity.applicationContext,
                    it,
                    Toast.LENGTH_SHORT
                ).show()
            })
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
            msg.postValue("下载成功")
            loading.postValue(false)
        }
    }

    /**
     * 显示图片
     */
    @JavascriptInterface
    fun showImage(
        url: String,
        x: Double,
        y: Double,
        width: Double,
        height: Double,
        clientWidth: Double
    ) {
        //发送显示图片消息
        image.postValue(
            PositionImage(
                url,
                x,
                y,
                width,
                height,
                clientWidth
            )
        )
    }
}