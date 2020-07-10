package io.github.iamyours.wandroid.net.wan

import androidx.annotation.Keep

@Keep
data class WanResponse<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)