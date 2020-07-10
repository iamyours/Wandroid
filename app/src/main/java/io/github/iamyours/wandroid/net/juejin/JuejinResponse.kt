package io.github.iamyours.wandroid.net.juejin

import androidx.annotation.Keep

@Keep
data class JuejinResponse<T>(
    val s: Int,
    val m: String,
    val d: T?
)