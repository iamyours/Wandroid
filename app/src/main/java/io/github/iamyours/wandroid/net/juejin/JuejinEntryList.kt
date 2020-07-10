package io.github.iamyours.wandroid.net.juejin

data class JuejinEntryList<T>(
    val total: Int,
    val entrylist: List<T>
)