package io.github.iamyours.wandroid.util

import android.text.TextUtils
import io.github.iamyours.wandroid.App
import io.github.iamyours.wandroid.BuildConfig
import io.github.iamyours.wandroid.extension.logE
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.charset.Charset


object FileUtil {
    private fun md5(string: String): String? {
        return MD5Utils.stringToMD5(string)
    }

    fun readStringInAssets(path: String): String {
        val input = App.instance.assets.open(path)
        val len = input.available();
        val buffer = ByteArray(len)
        input.read(buffer)
        return String(buffer, Charset.forName("utf-8"))
    }


    fun saveHtml(url: String, html: String) {
        val name = "${md5(url)}.html"
        val htmlPath = "/data/data/${BuildConfig.APPLICATION_ID}/html"
        val file = File("$htmlPath/resources")
        if (!file.exists()) file.mkdirs()
        writeString2File("$htmlPath/$name", html)
    }

    private fun download2File(file: File, resUrl: String) {
        "download...$resUrl".logE()
        if (TextUtils.isEmpty(resUrl)) return
        val ext = resUrl.substring(resUrl.lastIndexOf("."))
        val md5 = md5(resUrl) + ext
        val client = OkHttpClient.Builder().build()
        val req = Request.Builder().url(resUrl).build()
        val call = client.newCall(req)
        val result = call.execute()
        result.body()?.byteStream()?.let {
            val file = File(file, md5)
            val fos = FileOutputStream(file)
            fos.write(it.readBytes())
            fos.flush()
            fos.close()
        }
    }

    private fun writeString2File(path: String, html: String) {
        val file = File(path)
        val fos = FileOutputStream(file)
        fos.write(html.toByteArray())
        fos.flush()
        fos.close()
        "writeString2File:$path".logE()
    }

    fun getHtmlFile(fileName: String): String {
        return "/data/data/${BuildConfig.APPLICATION_ID}/html/$fileName"
    }

    fun getHtml(url: String): String? {
        val path = getHtmlFile("${md5(url)}.html")
        return getString(path)
    }

    fun getString(fileName: String): String? {
        val file = File(fileName)
        if (file.exists()) {
            val fis = FileInputStream(file)
            return String(fis.readBytes())
        }
        return null;
    }
}