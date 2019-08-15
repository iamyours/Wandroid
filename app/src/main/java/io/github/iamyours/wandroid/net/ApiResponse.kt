package io.github.iamyours.wandroid.net

data class ApiResponse<T>(
    var data: T?,
    var errorCode: Int,
    var errorMsg: String
)