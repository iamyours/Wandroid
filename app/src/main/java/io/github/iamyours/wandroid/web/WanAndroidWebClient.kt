package io.github.iamyours.wandroid.web

import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData

/**
 * 适配WanAndroid网站手机端
 */
class WanAndroidWebClient(vo: MutableLiveData<Boolean>) : BaseWebViewClient(vo) {
    private val articleByAuthor =
        "https://www.wanandroid.com/article/list/0?author="
    private val articleByCapter =
        "https://www.wanandroid.com/article/list_by_chapter/1"

    private val cssFiles = arrayOf(
        "blog/default.css", "pc/common.css",
        "pc/header.css", "wenda/wenda_md.css", "m/common.css"
    )

    override fun shouldInterceptRequest(view: WebView?, url: String?)
            : WebResourceResponse? {
        Log.i("wenda", "url:$url")
        cssFiles.forEach {
            if ((url ?: "").contains(it)) {
                val stream = view!!.context.assets.open("wanandroid/css/$it")
                Log.e("wanandroid", "load resource from local $it")
                return WebResourceResponse("text/css", "utf-8", stream)
            }
        }
        return super.shouldInterceptRequest(view, url)
    }

}