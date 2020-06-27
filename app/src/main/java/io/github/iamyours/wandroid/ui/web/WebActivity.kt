package io.github.iamyours.wandroid.ui.web

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.lifecycle.Observer
import io.github.iamyours.router.ARouter
import io.github.iamyours.router.annotation.Route
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.R
import io.github.iamyours.wandroid.base.BaseActivity
import io.github.iamyours.wandroid.databinding.ActivityWebBinding
import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.extension.*
import io.github.iamyours.wandroid.util.Constants
import io.github.iamyours.wandroid.util.FileUtil
import io.github.iamyours.wandroid.util.WebViewUtil
import io.github.iamyours.wandroid.vo.CacheArticleVO
import io.github.iamyours.wandroid.web.WanObject
import io.github.iamyours.wandroid.web.WebViewClientFactory
import io.github.iamyours.wandroid.widget.BottomStyleDialog
import io.github.iamyours.wandroid.widget.WanWebView
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.dialog_more.view.*

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
    val cacheDao = AppDataBase.get().cacheDao()

    val vm by viewModel<WebVM> {
        collect.value = intent.getBooleanExtra("collect", false)
        articleId.value = intent.getIntExtra("articleId", 0)
    }
    var navTitle = ""

    override fun onEnterAnimationComplete() {
        super.onEnterAnimationComplete()
        binding.showImage.visibility = View.INVISIBLE
    }

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
        vm.loaded.observe(this, Observer {
            if (it) {
                loadBaseScript(webView)
            }
        })
        binding.showImage.setDisableTouch(true)
    }

    private fun loadBaseScript(webView: WebView) {
        //显示图片,阻止事件冒泡（CSDN图片显示）
        "loadBaseScript...".logE()
        //代码图片展示 todo 部分站点显示有问题
        val script = """
            javascript:(function(){
                var imgs = document.getElementsByTagName("img");
                for(var i=0;i<imgs.length;i++){
                    var dataset = imgs[i].dataset;
                    if(dataset && dataset.src && dataset.src!=imgs[i].src){
                        imgs[i].src = dataset.src;
                    }
                    imgs[i].onclick = function(e){
                        var target = e.target;
                        var rect = target.getBoundingClientRect();
                        android.showImage(target.src,rect.x,rect.y,rect.width,rect.height,outerWidth);
                        e.stopPropagation();
                    };
                }
               var domScript = document.createElement("script");
                domScript.setAttribute("type","text/javascript");
                domScript.setAttribute("src","https://iamyours.com/dom-to-image.min.js");
                document.body.appendChild(domScript);
                
                var pres = document.getElementsByTagName("pre");
                for(var i=0;i<pres.length;i++){
                    pres[i].onclick = function(e){
                        console.log(this.tagName);
                        var rect = this.getBoundingClientRect();
                        var imgWidth = this.scrollWidth + 15;
                        var childWidth = this.children[0].scrollWidth||0;
                        if(childWidth>imgWidth)imgWidth=childWidth;
                        domtoimage.toPng(this,{width:imgWidth}).then(function(data){
                            android.showImage(data,rect.x,rect.y,rect.width,rect.height,outerWidth);
                        });
                    };
                }
            })();
        """.trimIndent()
        webView.postDelayed({
            webView.loadUrl(script)
        }, 300)

    }

    private fun showMoreDialog() {
        val v = LayoutInflater.from(this).inflate(R.layout.dialog_more, null)
        val dialog = BottomStyleDialog(this)
        dialog.setContentView(v)
        val cached = cacheDao.hasCache(link ?: "")
        if (vm.articleId.value == 0) {
            v.dv_collect.visibility = View.GONE
        }
        v.dv_download.isSelected = cached
        v.dtv_download.text = if (cached) "已下载" else "下载"
        v.dv_download.setOnClickListener {
            downHtml()
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
            cacheMode = WebSettings.LOAD_NO_CACHE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }
        }
        binding.webView.run {
            WebViewUtil.fixWebView(this)
            setBackgroundColor(0)
            loadWeb(this, link)
            webViewClient = WebViewClientFactory.create(link!!, vm.loaded)
            webChromeClient = object : WebChromeClient() {
                override fun onReceivedTitle(view: WebView?, t: String?) {
                    super.onReceivedTitle(view, t)
                    navTitle = t ?: ""
                }
            }
            addJavascriptInterface(
                WanObject(this@WebActivity, vm.image),
                "android"
            )
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

    private fun loadWeb(webview: WanWebView, url: String?) {
        webview.loadUrl(url)
    }


    /**
     * 保存css/图片/html内容
     */
    private fun downHtml() {
        val script = """
            javascript:(function(){
                var url = document.URL.toString();
                var html = document.documentElement.outerHTML;
                android.saveHtml(url,html);
            })();
        """.trimIndent()
        webView.loadUrl(script)
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
}