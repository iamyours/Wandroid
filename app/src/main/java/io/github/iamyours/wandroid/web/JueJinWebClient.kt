package io.github.iamyours.wandroid.web

import android.os.Handler
import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData

/**
 * 掘金app适配
 */
class JueJinWebClient(vo: MutableLiveData<Boolean>) : BaseWebViewClient(vo) {
    val script = """
        javascript:(function(){
            var arr = document.getElementsByClassName("lazyload");
            for(var i=0;i<arr.length;i++){
                var img = arr[i];
                var src = img.getAttribute("data-src");
                img.src = src;
            }
        })();
    """.trimIndent()
    var load = false
    val handler = Handler()
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if ((url ?: "").startsWith("https://juejin.im/post/")) {
            if (load) return
            handler.postDelayed({
                view?.loadUrl(script)
                load = true
            },100)
        }
    }

    override fun shouldInterceptRequest(view: WebView?, url: String?)
            : WebResourceResponse? {
        Log.i("掘金", "url:$url")
        val urlStr = url ?: ""
        if (urlStr.startsWith("https://b-gold-cdn.xitu.io/v3/static/css/0")
            && urlStr.endsWith(".css")
        ) {
            val stream = view!!.context.assets.open("juejin/css/juejin.css")
            return WebResourceResponse("text/css", "utf-8", stream)
        }

        return super.shouldInterceptRequest(view, url)
    }
}