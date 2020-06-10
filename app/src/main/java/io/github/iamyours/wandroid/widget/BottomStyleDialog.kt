package io.github.iamyours.wandroid.widget

import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialog
import io.github.iamyours.wandroid.R

class BottomStyleDialog(context: Context) : AppCompatDialog(context, R.style.BottomDialogs){
    init {
        window.decorView.setPadding(0,0,0,0)
        val attr = window.attributes
        attr.width = ViewGroup.LayoutParams.MATCH_PARENT
        attr.height = ViewGroup.LayoutParams.WRAP_CONTENT
        attr.gravity = Gravity.BOTTOM+Gravity.CENTER_HORIZONTAL
        window.attributes = attr
    }
}
