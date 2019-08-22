package io.github.iamyours.wandroid.ui.web

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.webkit.WebView
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityWebBinding
import io.github.iamyours.wandroid.extension.arg
import io.github.iamyours.wandroid.web.WanAndroidWebClient
import io.github.iamyours.wandroid.web.WebViewClientFactory

class WebActivity : BaseActivity<ActivityWebBinding>() {
    companion object {
        fun nav(link: String, context: Context) {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("link", link)
            if (context is Activity) {
                context.startActivityForResult(intent, 1)
            }
        }
    }

    override val layoutId: Int
        get() = R.layout.activity_web
    val link by arg<String>("link")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWebView()
    }

    private fun initWebView() {
        binding.webView.settings.run {
            javaScriptEnabled = true
        }
        binding.webView.run {
            loadUrl(link)
            webViewClient = WebViewClientFactory.create(url)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
    }
}