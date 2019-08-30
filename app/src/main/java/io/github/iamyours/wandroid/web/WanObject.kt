package io.github.iamyours.wandroid.web

import android.content.Context
import android.content.Intent
import android.webkit.JavascriptInterface
import io.github.iamyours.wandroid.ui.login.LoginActivity

class WanObject(var context: Context) {

    @JavascriptInterface
    fun toLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        context.startActivity(intent)
    }
}