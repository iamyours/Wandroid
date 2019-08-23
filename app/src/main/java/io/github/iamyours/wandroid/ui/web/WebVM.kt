package io.github.iamyours.wandroid.ui.web

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WebVM : ViewModel() {
    val loaded = MutableLiveData<Boolean>()
    val title = MutableLiveData<String>()
}