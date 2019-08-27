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
 * weixin暗黑模式
 */
class WeiXinWebClient(url: String, vo: MutableLiveData<Boolean>) :
    BaseWebViewClient(url, vo) {

    override fun shouldInterceptRequest(view: WebView?, url: String?)
            : WebResourceResponse? {
        val urlStr = url ?: ""
        if (urlStr.startsWith(WebViewClientFactory.WEI_XIN)) {
            val response = Wget.get(url ?: "")
            val res = replaceCss(response, view!!.context)
            val input = ByteArrayInputStream(res.toByteArray())
            return WebResourceResponse("text/html", "utf-8", input)
        }
        return super.shouldInterceptRequest(view, url)
    }

    private val rex =
        "(<style>)([\\S ]*)(</style>)"

    private fun replaceCss(res: String, context: Context): String {
        val pattern = Pattern.compile(rex)
        val m = pattern.matcher(res)
        return if (m.find()) {
            val css =
                StringUtil.getString(context.assets.open("weixin/weixin.css"))
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