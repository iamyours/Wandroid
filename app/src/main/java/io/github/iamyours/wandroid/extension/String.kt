package io.github.iamyours.wandroid.extension

import android.util.Log
import io.github.iamyours.wandroid.BuildConfig

fun String.logE() {
    if (BuildConfig.DEBUG) {
        Log.e("Wandroid", this)
    }
}