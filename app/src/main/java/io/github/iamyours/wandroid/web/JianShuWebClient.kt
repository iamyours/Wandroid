package io.github.iamyours.wandroid.web

import android.content.Context
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import io.github.iamyours.wandroid.extension.logE
import io.github.iamyours.wandroid.util.FileUtil
import io.github.iamyours.wandroid.util.StringUtil
import io.github.iamyours.wandroid.util.Wget
import java.io.ByteArrayInputStream
import java.util.regex.Pattern

/**
 * 简书app适配
 */
class JianShuWebClient(url: String, vo: MutableLiveData<Boolean>) :
    BaseWebViewClient(url, vo) {

    override fun shouldInterceptRequest(view: WebView?, url: String?)
            : WebResourceResponse? {
        val urlStr = url ?: ""
        if (urlStr.startsWith("https://www.jianshu.com/p/")) {
            val cache = FileUtil.getHtml(originUrl)
            val response = cache ?: Wget.get(url ?: "")
            val res = cache ?: darkBody(replaceCss(response, view!!.context))
            val input = ByteArrayInputStream(res.toByteArray())
            return WebResourceResponse("text/html", "utf-8", input)
        }
        return super.shouldInterceptRequest(view, url)
    }

    private val rex =
        "(<style data-vue-ssr-id=[\\s\\S]*?>)([\\s\\S]*]?)(<\\/style>)"
    private val bodyRex = "<body class=\"([\\s\\S]*?)\""
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
            val css =
                StringUtil.getString(context.assets.open("jianshu/jianshu.css"))
            val sb = StringBuilder()
            sb.append(m.group(1))
            sb.append(css)
            sb.append(m.group(3))
            val res = res.replace(rex.toRegex(), sb.toString())
            res
        } else {
            res
        }
    }
}