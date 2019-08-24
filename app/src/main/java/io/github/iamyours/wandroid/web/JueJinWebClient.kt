package io.github.iamyours.wandroid.web

import android.os.Handler
import android.os.Message
import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException
import java.lang.ref.WeakReference

/**
 * 掘金app适配
 */
class JueJinWebClient(url: String, vo: MutableLiveData<Boolean>) :
    BaseWebViewClient
        (url, vo) {
    var detailApi = ""


    var load = false
    val handler = Handler()
    val juejinUrl = "https://juejin.im/post/"
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if ((url ?: "").startsWith(juejinUrl)) {
            if (load) return
            val postId = url?.substring(juejinUrl.length) ?: ""
            detailApi = getDetailApi(postId)
            loadUser()
            ImageLoaderHandler(view!!).sendEmptyMessageDelayed(1, 60)
        }
    }

    inner class ImageLoaderHandler(webView: WebView) : Handler() {
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
        var time = 0
        var webViewRef: WeakReference<WebView>? = WeakReference(webView)
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            if (time > 5) return
            time++
            webViewRef?.get()?.loadUrl(script)
            loadUserScript()
            sendEmptyMessageDelayed(1, 60)
        }

        fun loadUserScript() {
            val script = """
                javascript:(function(){
                    document.getElementsByClassName("author-info-block")[0].children[0].children[0].style.backgroundImage = "url('$head')";
                    document.getElementsByClassName("username")[0].innerHTML="$username";
                })();
            """.trimIndent()
            webViewRef?.get()?.loadUrl(script)
        }
    }

    private var head = ""
    private var username = ""

    private fun loadUser() {
        val client = OkHttpClient.Builder().build()
        val req = Request.Builder().url(detailApi).build()
        val call = client.newCall(req)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
            }

            override fun onResponse(call: Call, response: Response) {
                val res = response.body()?.string() ?: "{}"
                val obj =
                    Gson().fromJson<JsonObject>(res, JsonObject::class.java)
                obj?.getAsJsonObject("d")
                    ?.getAsJsonObject("user")?.run {
                        head = get("avatarLarge").asString
                        username = get("username").asString
                    }

            }
        })
    }


    private fun getDetailApi(postId: String): String {//头像没有加载，手动调用
        return "https://post-storage-api-ms.juejin" +
                ".im/v1/getDetailData?src=web&type=entry&postId=$postId"
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