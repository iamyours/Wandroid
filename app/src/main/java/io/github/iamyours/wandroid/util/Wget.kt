package io.github.iamyours.wandroid.util

import io.github.iamyours.wandroid.extension.logE
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.InputStream

object Wget {
    fun get(url: String): String {
        val client = OkHttpClient.Builder()
            .build()
        val request = Request.Builder()
            .url(url)
            .header(
                "user-agent",
                "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3887.7 Mobile Safari/537.36"
            )
            .build()
        val response = client.newCall(request).execute()
        return response.body()?.string() ?: ""
    }

    fun head(url: String?): String {
        val client = OkHttpClient.Builder()
            .build()
        val request = Request.Builder()
            .url(url)
            .head()
            .build()
        val t = System.currentTimeMillis()
        val res = client.newCall(request).execute()
        val time = System.currentTimeMillis() - t
        val type = res.header("content-type")
        "time:$time,$type"
        return type ?: ""
    }
}