package io.github.iamyours.wandroid.web

import android.os.Handler
import android.util.Log
import android.webkit.WebView

/**
 * 掘金app适配
 */
class JueJinWebClient : BaseWebViewClient() {
    val script = """
        javascript:(function(){
//            document.getElementsByClassName("open-in-app")[0].style.display="none";
//            document.getElementsByClassName("main-header-box")[0].style.display = "none";
            document.getElementsByClassName("action-box action-bar")[0].style.display = "none";
        })();
    """
    var load = false

    private val handler = Handler()
    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        if (load) return
        if (url.startsWith("https://juejin.im/post/")) {
            Log.i("juejin", "loadScript:$script")
            handler.postDelayed({
                view.loadUrl(script)
                load = true
            },300)
        }
    }
}