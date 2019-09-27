package io.github.iamyours.wandroid.web

import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData

class CsdnWebViewClient(url: String, vo: MutableLiveData<Boolean>) :
    BaseWebViewClient(url, vo) {
    override fun shouldInterceptRequest(view: WebView?, url: String?)
            : WebResourceResponse? {
        Log.i("csdn", "url:$url")
        val urlStr = url ?: ""
        if (urlStr.endsWith(".css")) {
            if (urlStr.startsWith("https://csdnimg.cn/release/phoenix/production/mobile_detail_style")

            ) {
                val stream =
                    view!!.context.assets.open("csdn/mobile_detail.css")
                return WebResourceResponse("text/css", "utf-8", stream)
            }
            if (urlStr.startsWith("https://csdnimg.cn/release/phoenix/production/wapedit_views_md")

            ) {
                val stream =
                    view!!.context.assets.open("csdn/md.css")
                return WebResourceResponse("text/css", "utf-8", stream)
            }
            if (urlStr.startsWith("https://csdnimg.cn/release/phoenix/template")) {
                if (urlStr.contains("wap_detail_view") && urlStr.endsWith(".css")) {
                    val stream =
                        view!!.context.assets.open("csdn/wap_detail_view.css")
                    return WebResourceResponse("text/css", "utf-8", stream)
                }
            }
        }

        return super.shouldInterceptRequest(view, url)
    }
}