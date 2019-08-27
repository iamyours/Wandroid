package io.github.iamyours.wandroid.web

import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData

object WebViewClientFactory {
    val WAN_ANDROID = "wanandroid.com"
    val JIAN_SHU = "https://www.jianshu.com"
    val JUE_JIN = "https://juejin.im/post/"
    val WEI_XIN = "https://mp.weixin.qq.com/s"
    fun create(url: String, vo: MutableLiveData<Boolean>): WebViewClient {
        return when {
            url.contains(WAN_ANDROID) -> WanAndroidWebClient(url, vo)
            url.startsWith(JIAN_SHU) -> JianShuWebClient(url, vo)
            url.startsWith(JUE_JIN) -> JueJinWebClient(url, vo)
            url.startsWith(WEI_XIN) -> WeiXinWebClient(url, vo)
            else -> object : BaseWebViewClient(url, vo) {}
        }
    }
}