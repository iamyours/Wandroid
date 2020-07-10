package io.github.iamyours.wandroid.net.xxmh

import io.github.iamyours.wandroid.net.IResponse

class XResponse<T>(
    override val msg: String?,
    override val code: Int,
    val content: T?
) : IResponse<T> {
    override val data: T?
        get() = content
}