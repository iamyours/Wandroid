package io.github.iamyours.wandroid.web

import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData

object WebViewClientFactory {
    val WAN_ANDROID = "www.wanandroid.com"
    val JIAN_SHU = "https://www.jianshu.com"
    val JUE_JIN = "https://juejin.im/post/"
    fun create(url: String, vo: MutableLiveData<Boolean>): WebViewClient {
        return when {
            url.contains(WAN_ANDROID) -> WanAndroidWebClient(vo)
            url.startsWith(JIAN_SHU) -> JianShuWebClient(vo)
            url.startsWith(JUE_JIN) -> JueJinWebClient(vo)
            else -> object : BaseWebViewClient(vo) {}
        }
    }
}