package io.github.iamyours.wandroid.ui.web

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.Observer
import io.github.iamyours.router.ARouter
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityWeb2Binding
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.extension.*
import io.github.iamyours.wandroid.util.*
import io.github.iamyours.wandroid.util.glide.GlideUtil
import io.github.iamyours.wandroid.vo.CacheArticleVO
import io.github.iamyours.wandroid.web.WanObject
import io.github.iamyours.wandroid.widget.BottomStyleDialog
import io.github.iamyours.wandroid.widget.WanWebView
import kotlinx.android.synthetic.main.dialog_more.view.*
import java.io.ByteArrayInputStream

@Route(path = "/web2")
class Web2Activity : BaseActivity<ActivityWeb2Binding>() {
    override val layoutId: Int
        get() = R.layout.activity_web2
    val link by arg<String>("link")
    var navTitle = ""
    val cacheDao = AppDataBase.get().cacheDao()

    val vm by viewModel<Web2VM> {
        collect.value = intent.getBooleanExtra("collect", false)
        articleId.value = intent.getIntExtra("articleId", 0)
        articleUrl.value = link
    }

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        binding.showImage.visibility = View.INVISIBLE
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.showImage.postDelayed({
            binding.showImage.visibility = View.INVISIBLE
        }, 350)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        vm.attachLoading(loadingState)
        vm.collect.observe(this, Observer {
            val data = Intent()
            data.putExtra("collect", vm.collect.value ?: false)
            setResult(Constants.RESULT_COLLECT_CHANGED, data)
        })
        vm.errorMsg.observe(this, Observer {
            it.toast()
        })
        vm.toLogin.observe(this, Observer {
            ARouter.getInstance()
                .build("/login")
                .navigation(this) { _, resultCode, _ ->
                    if (resultCode == Constants.RESULT_LOGIN) {
                        vm.isLogin.value = true
                    }
                }
        })
        vm.showMore.observe(this, Observer {
            showMoreDialog()
        })
        initWebView()
        vm.loaded.observe(this, Observer {
            if (it) {
                loadScript()
            }
        })
    }

    override fun onBackPressed() {
        backAction()
    }

    private fun backAction() {
        finish()
    }

    private fun loadScript() {
        binding.webView.let {
            val script = Script.getDom2Image(it.width)
            it.evaluateJavascript(script) {}
        }
    }

    private fun initWebView() {
        binding.webView.run {
            settings.run {
                javaScriptEnabled = true
            }
            setBackgroundColor(0)
            addJavascriptInterface(
                WanObject(context, vm.image),
                "android"
            )
            scrollListener = object : WanWebView.OnScrollChangedListener {
                override fun onScroll(dx: Int, dy: Int, oldX: Int, oldY: Int) {
                    vm.title.value = if (dy < 10) "" else navTitle
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
            }
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView,
                    url: String
                ): WebResourceResponse? {
                    if (url == link) {
                        val cachedString = FileUtil.getHtml(url)
                        val html =
                            cachedString ?: JsoupUtil.parseArticleHtml(url)
                        val input = ByteArrayInputStream(html.toByteArray())
                        return WebResourceResponse("text/html", "utf-8", input)
                    } else if (url.endsWith("resources/js/highlight.pack.js")) {
                        val html =
                            FileUtil.readStringInAssets("js/highlight.min.js")
                        val input = ByteArrayInputStream(html.toByteArray())
                        return WebResourceResponse(
                            "text/javascript",
                            "utf-8",
                            input
                        )
                    } else if (url.endsWith("resources/js/dom-to-image.js")) {
                        val html =
                            FileUtil.readStringInAssets("dom-to-image.min.js")
                        val input = ByteArrayInputStream(html.toByteArray())
                        return WebResourceResponse(
                            "text/javascript",
                            "utf-8",
                            input
                        )
                    } else if (url.startsWith("http")) {
                        val head = Wget.head(url)
                        if (head.startsWith("image")) {
                            val bytes = GlideUtil.syncLoad(url, head)
                            if (bytes != null) {
                                return WebResourceResponse(
                                    head,
                                    "utf-8",
                                    ByteArrayInputStream(bytes)
                                )
                            }
                        }
                    }
                    return super.shouldInterceptRequest(view, url)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView,
                    url: String
                ): Boolean {
                    val isHttp =
                        url.startsWith("http://") || url.startsWith("https://")
                    return if (isHttp) {
                        RouterUtil.navWeb2(url, view.context)
                        true
                    } else {
                        true
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    vm.loaded.value = true
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, title: String) {
                    super.onReceivedTitle(view, title)
                    navTitle = title
                    vm.title.value = title
                }
            }
            loadUrl(link)
        }
    }

    private fun showMoreDialog() {
        val v = LayoutInflater.from(this).inflate(R.layout.dialog_more, null)
        val dialog = BottomStyleDialog(this)
        dialog.setContentView(v)
        val cached = cacheDao.hasCache(link ?: "")
        v.dv_download.isSelected = cached
        v.dtv_download.text = if (cached) "已下载" else "下载"
        v.dv_download.setOnClickListener {
            binding.webView.evaluateJavascript(Script.downloadHtmlScript(cached)) {}
            saveCacheOrNot(link, navTitle, cached)
            dialog.dismiss()
        }
        v.dv_link.setOnClickListener {
            link?.copy(it.context)
            dialog.dismiss()
        }
        v.dv_open_link.setOnClickListener {
            link?.openBrowser(it.context)
            dialog.dismiss()
        }
        v.dv_collect.isSelected = vm.collect.value ?: false
        v.dv_collect.setOnClickListener {
            vm.collectOrNot()
            dialog.dismiss()
        }
        v.dtv_cancel.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun saveCacheOrNot(
        link: String?,
        navTitle: String,
        cached: Boolean
    ) {
        link?.let {
            if (cached) {
                cacheDao.delete(link)
            } else {
                val cache =
                    CacheArticleVO(it, navTitle, System.currentTimeMillis())
                cacheDao.add(cache)
            }
        }
    }
}