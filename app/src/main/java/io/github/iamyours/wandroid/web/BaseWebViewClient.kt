package io.github.iamyours.wandroid.web

import android.content.Intent
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.ui.web.WebActivity
import io.github.iamyours.wandroid.vo.WebViewVO
import java.net.URISyntaxException

open class BaseWebViewClient(
    protected var originUrl: String, private var vo:
    MutableLiveData<Boolean>
) :
    WebViewClient() {
    override fun shouldOverrideUrlLoading(
        view: WebView,
        url: String?
    ): Boolean {
        if (url == null) {
            return false
        }
        if (url.startsWith("intent")) run {
            try {
                val context = view.context
                val intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME)
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(
                        context.applicationContext, "无法打开", Toast
                            .LENGTH_SHORT
                    ).show()
                }
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }

            return true
        }
        val isHttp = url.startsWith("http://") || url.startsWith("https://")
        val fileName = url.substring(url.lastIndexOf("/"))
        val isResource = fileName.contains(".")
        val originName = originUrl.substring(originUrl.lastIndexOf("/"))
        //有些实际url相同，只不过http/https格式不一样
        if (fileName == originName) return super.shouldOverrideUrlLoading(
            view,
            url
        )
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

    override fun onReceivedSslError(
        view: WebView?,
        handler: SslErrorHandler?,
        error: SslError?
    ) {
        super.onReceivedSslError(view, handler, error)
    }
}