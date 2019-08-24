package io.github.iamyours.wandroid.widget

import android.content.Context
import android.util.AttributeSet
import android.webkit.WebView

class WanWebView(context: Context, attributeSet: AttributeSet) : WebView
    (context, attributeSet) {
    var scrollListener: OnScrollChangedListener? = null
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        scrollListener?.onScroll(l, t, oldl, oldt)
    }

    interface OnScrollChangedListener {
        fun onScroll(dx: Int, dy: Int, oldX: Int, oldY: Int)
    }
}