package io.github.iamyours.wandroid.ui.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebVM : ViewModel() {
    val loaded = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
    val collect = MutableLiveData<Boolean>()

    fun collectOrNot() {
        collect.value = !(collect.value ?: false)
    }
}