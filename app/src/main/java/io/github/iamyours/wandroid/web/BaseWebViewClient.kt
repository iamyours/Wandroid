package io.github.iamyours.wandroid.web

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.ui.web.WebActivity
import io.github.iamyours.wandroid.vo.WebViewVO

open class BaseWebViewClient(private var vo: MutableLiveData<Boolean>) :
    WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String?
    ): Boolean {
        if (url == null) {
            return false
        }
        val isHttp = url.startsWith("http://") || url.startsWith("https://")
        val isResource = url.contains("/[\\s\\S]*?\\.[\\s\\S]*?$".toRegex())
        return if (isHttp && !isResource) {
            WebActivity.nav(url, view.context)
            true
        } else
            super.shouldOverrideUrlLoading(view, url ?: "")
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        vo.value = true

    }
}