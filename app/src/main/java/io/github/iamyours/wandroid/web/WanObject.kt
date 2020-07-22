package io.github.iamyours.wandroid.web

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.github.iamyours.wandroid.extension.logE
import io.github.iamyours.wandroid.extension.logV
import io.github.iamyours.wandroid.observer.LoadingObserver
import io.github.iamyours.wandroid.ui.login.LoginActivity
import io.github.iamyours.wandroid.util.Constants
import io.github.iamyours.wandroid.util.FileUtil
import io.github.iamyours.wandroid.util.Wget
import io.github.iamyours.wandroid.util.glide.GlideUtil

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

    @JavascriptInterface
    fun test(obj: Any) {
        "$obj".logE()
    }

    /**
     * 离线保存html
     */
    @JavascriptInterface
    fun saveHtml(url: String, html: String, urls: String, cached: Boolean) {
        loading.postValue(true)
        Constants.IO.execute {
            if (cached) {
                FileUtil.deleteHtml(url)
            } else
                FileUtil.saveHtml(url, html)
            urls.split(", ").forEach {
                var imgUrl = it
                if (imgUrl.startsWith("//")) {//简书图片懒加载地址
                    imgUrl = "https:$imgUrl"
                }
                if (!TextUtils.isEmpty(imgUrl) && !imgUrl.startsWith("data:image")) {
                    if (cached) {
                        GlideUtil.removePermanent(imgUrl)
                    } else {
                        if (!GlideUtil.cacheToPermanent(imgUrl)) {//缓存区没有，重新下载
                            val tmp = GlideUtil.permanentTempName(imgUrl)
                            if (Wget.download(imgUrl, tmp)) {
                                "download success:$url".logV()
                                GlideUtil.tempToPermanent(imgUrl)
                            } else {
                                "download fail:$url".logE()
                            }
                        }
                    }
                }
            }
            msg.postValue(if (cached) "删除成功" else "下载成功")
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
        "showImage:${url.length},w:$width,h:$height".logE()
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