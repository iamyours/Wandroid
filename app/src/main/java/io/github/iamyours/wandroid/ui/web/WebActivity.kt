package io.github.iamyours.wandroid.ui.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityWebBinding
import io.github.iamyours.wandroid.extension.arg
import io.github.iamyours.wandroid.extension.viewModel
import io.github.iamyours.wandroid.util.Constants
import io.github.iamyours.wandroid.vo.WebViewVO
import io.github.iamyours.wandroid.web.WanAndroidWebClient
import io.github.iamyours.wandroid.web.WanObject
import io.github.iamyours.wandroid.web.WebViewClientFactory
import io.github.iamyours.wandroid.widget.WanWebView
import kotlinx.android.synthetic.main.activity_web.*

@Route(path = "/web")
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

    val vm by viewModel<WebVM> {
        collect.value = intent.getBooleanExtra("collect", false)
        articleId.value = intent.getIntExtra("articleId", 0)
    }
    var navTitle = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.vm = vm
        initWebView()
        vm.attachLoading(loadingState)
        vm.collect.observe(this, Observer {
            val data = Intent()
            data.putExtra("collect", vm.collect.value ?: false)
            setResult(Constants.RESULT_COLLECT_CHANGED, data)
        })
    }

    private var checkHeightHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (vm.loaded.value == true) {
                return
            }
            checkWebHeight()
            sendEmptyMessageDelayed(1, 60)
        }
    }

    private fun checkWebHeight() {//检查内容高度，隐藏加载进度
        binding.webView.run {
            if (contentHeight > height / 2) {
                vm.loaded.value = true
            }
        }
    }


    private fun initWebView() {
        binding.webView.settings.run {
            javaScriptEnabled = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        binding.webView.run {
            setBackgroundColor(0)
            loadUrl(link)
            webViewClient = WebViewClientFactory.create(url, vm.loaded)
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, t: String?) {
                    super.onReceivedTitle(view, t)
                    navTitle = t ?: ""
                }
            }
            addJavascriptInterface(WanObject(this@WebActivity), "android")
            scrollListener = object : WanWebView.OnScrollChangedListener {
                override fun onScroll(dx: Int, dy: Int, oldX: Int, oldY: Int) {
                    vm.title.value = if (dy < 10) "" else navTitle
                }

            }
            checkHeightHandler.sendEmptyMessageDelayed(1, 60)

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG)
        }
        if (link!!.contains(WebViewClientFactory.WAN_ANDROID)) {
            vm.loaded.observe(this, Observer {
                webView.loadUrl(getScript())
            })
        }
    }

    /**
     * 问答点赞功能
     */
    private fun getScript(): String {
        val id = link!!.substring(link!!.lastIndexOf("/") + 1)
        return """
        javascript:(function(){
            console.log(">>>>>>>>>>>>>>>>>>>>>");
            $(".zan_show").unbind();
            $(".zan_show").click(function(){
                var reg = /loginUserName=/g;
                var cookie = document.cookie;
                if(reg.test(cookie)){
                    var cid = $(this).attr("cid");
    		        var isZan = $(this).attr("is_zan");
                    ajaxZan(cid,"$id",isZan != 1);
                }else{
                    android.toLogin();
                }
            });
        })();
    """.trimIndent()
    }

}