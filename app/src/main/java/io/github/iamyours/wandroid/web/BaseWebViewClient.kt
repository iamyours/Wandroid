package io.github.iamyours.wandroid.web

import android.webkit.WebView
import android.webkit.WebViewClient
import io.github.iamyours.wandroid.ui.web.WebActivity

open class BaseWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
        if (url == null) {
            return false
        }
        val isHttp = url.startsWith("http://") || url.startsWith("https://")
        return if (isHttp) {
            WebActivity.nav(url, view.context)
            true
        } else
            super.shouldOverrideUrlLoading(view, url ?: "")
    }
}