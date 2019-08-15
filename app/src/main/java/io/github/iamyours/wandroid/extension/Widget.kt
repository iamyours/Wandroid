package io.github.iamyours.wandroid.extension

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions

fun ImageView.displayWithUrl(url: String?) {
    Glide.with(this).load(url)
        .apply(
            RequestOptions().transforms(CenterCrop())
        )
        .into(this)
}