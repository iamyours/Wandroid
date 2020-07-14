package io.github.iamyours.wandroid.util

import android.graphics.Rect
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.ImageView
import io.github.iamyours.wandroid.extension.logE


object ImageUtil {
    val handler = Handler(Looper.getMainLooper())

    fun checkImage(
        iv: ImageView,
        rect: Rect,
        callback: () -> Unit
    ) {
        if (iv.top == rect.top
            && iv.left == rect.left
            && iv.width == rect.width()
            && iv.height == rect.height()
        ) {
            callback()
        } else {
            handler.postDelayed({ checkImage(iv, rect, callback) }, 150)
        }
    }
}