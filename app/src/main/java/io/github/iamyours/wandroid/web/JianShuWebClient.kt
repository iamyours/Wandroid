package io.github.iamyours.wandroid.web

import android.util.Log
import android.webkit.WebView

/**
 * 简书app适配
 */
class JianShuWebClient : BaseWebViewClient() {
    val script = """
        javascript:(function(){
            document.getElementsByClassName("close-collapse-btn")[0].click();
            document.getElementsByClassName("close")[0].click();
            document.getElementsByClassName("note-comment-above-ad-wrap")[0].style.display="none";
            document.getElementsByClassName("header-wrap")[0].style.display = "none"
        })();
    """
    var load = false

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        if (load) return
        if (url.startsWith("https://www.jianshu.com/p/")) {
            Log.i("jianshu", "loadScript:$script")
            view.loadUrl(script)
            load = true
        }
    }

}