package io.github.iamyours.wandroid.net

interface IResponse<T> {
    val data: T?
    val msg: String?
    val code: Int
}