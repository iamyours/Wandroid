package io.github.iamyours.wandroid.web

import android.content.Context
import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.util.StringUtil
import io.github.iamyours.wandroid.util.Wget
import java.io.ByteArrayInputStream
import java.util.regex.Pattern

/**
 * 简书app适配
 */
class JianShuWebClient(vo: MutableLiveData<Boolean>) : BaseWebViewClient(vo) {
//    val script = """
//        javascript:(function(){
//            document.getElementsByClassName("switch                                                   -btn")[0].click();
//        })();
//    """
//    var load = false
//
//    override fun onPageFinished(view: WebView, url: String) {
//        super.onPageFinished(view, url)
//        if (load) return
//        if (url.startsWith("https://www.jianshu.com/p/")) {
//            Log.i("jianshu", "loadScript:$script")
//            view.loadUrl(script)
//            load = true
//        }
//    }

    override fun shouldInterceptRequest(view: WebView?, url: String?)
            : WebResourceResponse? {
        Log.i("jianshu", "url:$url")
        val urlStr = url ?: ""
        if (urlStr.startsWith("https://www.jianshu.com/p/")) {
            val response = Wget.get(url ?: "")
            val res = darkBody(replaceCss(response, view!!.context))
            val input = ByteArrayInputStream(res.toByteArray())
            return WebResourceResponse("text/html", "utf-8", input)
        }
        return super.shouldInterceptRequest(view, url)
    }

    private val rex = "(<style data-vue-ssr-id=[\\s\\S]*?>)([\\s\\S]*]?)(<\\/style>)"
    private val bodyRex = "<body class=\"([\\ss\\S]*?)\""
    private fun darkBody(res: String): String {
        val pattern = Pattern.compile(bodyRex)
        val m = pattern.matcher(res)
        return if (m.find()) {
            val s = "<body class=\"reader-night-mode normal-size\""
            res.replace(bodyRex.toRegex(), s)
        } else res
    }

    private fun replaceCss(res: String, context: Context): String {
        val pattern = Pattern.compile(rex)
        val m = pattern.matcher(res)
        return if (m.find()) {
            val css = StringUtil.getString(context.assets.open("jianshu/jianshu.css"))
            val sb = StringBuilder()
            sb.append(m.group(1))
            sb.append(css)
            sb.append(m.group(3))
            val res = res.replace(rex.toRegex(), sb.toString())
            Log.e("test", "$res")
            res
        } else {
            res
        }
    }
}