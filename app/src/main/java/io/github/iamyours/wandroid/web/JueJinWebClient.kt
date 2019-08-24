package io.github.iamyours.wandroid.web

import android.os.Handler
import android.util.Log
import android.webkit.WebResourceResponse
import android.webkit.WebView
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.*
import java.io.IOException

/**
 * 掘金app适配
 */
class JueJinWebClient(vo: MutableLiveData<Boolean>) : BaseWebViewClient(vo) {
    var detailApi = ""

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
    val juejinUrl = "https://juejin.im/post/"
    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if ((url ?: "").startsWith(juejinUrl)) {
            if (load) return
            val postId = url?.substring(juejinUrl.length) ?: ""
            detailApi = getDetailApi(postId)
            handler.postDelayed({
                view?.loadUrl(script)
                load = true
                loadUser(view!!)
            }, 100)
        }
    }

    private fun loadUser(webView: WebView) {
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
                updateUser(obj, webView)
            }
        })
    }

    private fun updateUser(obj: JsonObject?, webView: WebView) {
        val user = obj?.getAsJsonObject("d")
            ?.getAsJsonObject("user")
        user?.run {
            val head = get("avatarLarge").asString
            val username = get("username").asString
            val script = """
                javascript:(function(){
                    document.getElementsByClassName("author-info-block")[0].children[0].children[0].style.backgroundImage = "url('$head')";
                    document.getElementsByClassName("username")[0].innerHTML="$username";
                })();
            """.trimIndent()
            handler.post {
                webView.loadUrl(script)
            }
        }
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