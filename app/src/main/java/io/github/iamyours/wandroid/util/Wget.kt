package io.github.iamyours.wandroid.util

import io.github.iamyours.wandroid.db.AppDataBase
import io.github.iamyours.wandroid.extension.logE
import io.github.iamyours.wandroid.extension.logV
import io.github.iamyours.wandroid.net.CacheInterceptor
import io.github.iamyours.wandroid.net.RedirectInterceptor
import io.github.iamyours.wandroid.vo.UrlTypeVO
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Okio
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

object Wget {
    val headers = HashMap<String, String>()
    fun get(url: String): String {
        val client = OkHttpClient.Builder()
        ClientUtil.setUnsafe(client)
        client.followRedirects(false)
        client.followSslRedirects(false)
        client.addInterceptor(RedirectInterceptor())
        val request = Request.Builder()
            .url(url)
            .header(
                "User-Agent",
                "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Mobile Safari/537.36"
            )
            .build()
        val response = client.build().newCall(request).execute()
        val result = response.body()?.string() ?: ""
        return result
    }

    @JvmStatic
    fun getUnsafe(url: String): String {
        val request = Request.Builder().url(url).build()
        val response =
            ClientUtil.getUnsafeOkHttpClient().newCall(request).execute();
        return response.body()?.string() ?: ""
    }

    private val typeDao = AppDataBase.get().urlTypeDao()
    fun head(url: String?): String {
        val md5 = MD5Utils.stringToMD5(url)
        val value = headers[md5] ?: typeDao.getType(md5)
        "head:$url".logV()
        if (value == null) {
            val client = OkHttpClient.Builder()
                .addNetworkInterceptor(CacheInterceptor())
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Access-Control-Allow-Origin", "*")
                .addHeader("Access-Control-Allow-Headers", "X-Requested-With")
                .addHeader("Vary", "Accept-Encoding")
                .header(
                    "user-agent",
                    "Mozilla/5.0 (Linux; Android 8.0; Pixel 2 Build/OPD3.170816.012) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3887.7 Mobile Safari/537.36"
                )
                .head()
                .build()
            try {
                val t = System.currentTimeMillis()
                val res = client.newCall(request).execute()
                val time = System.currentTimeMillis() - t
                val type = res.header("content-type")
                "time:$time,$type".logE()
                val result = type ?: ""
                typeDao.insert(UrlTypeVO(md5, result))
                headers[md5] = result
                return result
            } catch (e: Exception) {
            }
        } else {
            headers[md5] = value
        }
        return value ?: ""
    }

    fun download(url: String, tmp: File): Boolean {
        "downloading...$url".logV()
        val request = Request.Builder().url(url).build()
        return ClientUtil.getUnsafeOkHttpClient().newCall(request).execute()
            ?.apply {
                val md5 = MD5Utils.stringToMD5(url)
                val type = header("content-type")
                val result = type ?: ""
                typeDao.insert(UrlTypeVO(md5, result))
            }?.body()?.run {

                val buffer = Okio.buffer(Okio.sink(tmp))
                buffer.writeAll(source())
                buffer.close()
                true
            } ?: false
    }
}