package io.github.iamyours.wandroid.web

import android.webkit.WebViewClient

object WebViewClientFactory {
    val WAN_ANDROID = "https://www.wanandroid.com"
    val JIAN_SHU = "https://www.jianshu.com"
    val JUE_JIN = "https://juejin.im/post/"
    fun create(url: String): WebViewClient {
        return when {
            url.startsWith(WAN_ANDROID) -> WanAndroidWebClient()
            url.startsWith(JIAN_SHU) -> JianShuWebClient()
            url.startsWith(JUE_JIN) -> JueJinWebClient()
            else -> object : BaseWebViewClient() {}
        }
    }
}