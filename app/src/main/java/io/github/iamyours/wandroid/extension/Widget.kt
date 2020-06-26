package io.github.iamyours.wandroid.extension

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import io.github.iamyours.wandroid.util.EmptyCornerDrawable
import io.github.iamyours.wandroid.util.glide.GlideApp

fun Context.getActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

fun Context.screenWidth(): Int {
    return resources.displayMetrics.widthPixels
}

fun Context.screenHeight(): Int {
    return resources.displayMetrics.heightPixels
}

fun View.getActivity(): Activity? {
    return context.getActivity()
}

fun ImageView.displayWithUrl(url: String?) {
    Glide.with(this).load(url)
        .apply(
            RequestOptions().transforms(CenterCrop())
        )
        .into(this)
}

fun ImageView.displayCenterInside(url: String?) {
    Glide.with(this)
        .asDrawable()
        .fitCenter()
        .load(url)
        .override(context.screenWidth(), context.screenHeight())
        .into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(
                resource: Drawable,
                transition: Transition<in Drawable>?
            ) {
                setImageDrawable(resource)
                if (resource is GifDrawable) {
                    resource.setVisible(true, true)
                    resource.start()
                }
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        })
}

fun ImageView.displayWithUrl2(url: String) {
    Glide.with(this).load(url)
        .into(this)
}

fun ImageView.displayBase64(url: String) {
    GlideApp.with(this)
        .load(url)
        .override(
            context.resources.displayMetrics.widthPixels,
            Target.SIZE_ORIGINAL
        )
        .into(this)
}

fun ImageView.displayOverride(
    url: String
) {
    Glide.with(this)
        .asBitmap()
        .load(url)
        .override(context.screenWidth(), context.screenHeight())
        .into(this)
}

fun ImageView.displayWithUrl(url: String?, radius: Float) {
    val radiusPx = radius.dp2IntPx(context)
    val empty = EmptyCornerDrawable(0xff969696.toInt(), radiusPx.toFloat())
    Glide.with(this).load(url)
        .apply(
            RequestOptions().transforms(
                CenterCrop(),
                RoundedCorners(radiusPx)
            )
                .placeholder(empty).error(
                    empty
                )
        )
        .into(this)
}


/**
 * 隐藏软键盘
 */
fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)

}