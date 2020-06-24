package io.github.iamyours.wandroid.util;

import android.os.Build;
import android.webkit.WebView;

import java.lang.reflect.Method;

public class WebViewUtil {
    public static void fixWebView(WebView webView){
        // webview的设置中添加如下代码
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                Class<?> clazz = webView.getSettings().getClass();
                Method method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", boolean.class);
                if (method != null) {
                    method.invoke(webView.getSettings(), true);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
