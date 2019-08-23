package io.github.iamyours.wandroid.util

import okhttp3.Cookie
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL

object Wget {
    fun get(url: String): String {
        val client = OkHttpClient.Builder()
            .build()
//        val httpUrl = HttpUrl.get(URL(url))!!
//        val list = client.cookieJar().loadForRequest(httpUrl)
//        val cookie = Cookie.Builder()
//            .name("read_mode").value("night")
//            .domain(httpUrl.host()).build()
//        try {
//            list.add(cookie)
//            client.cookieJar().saveFromResponse(httpUrl, list)
//        } catch (e: Exception) {
//        }
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
}