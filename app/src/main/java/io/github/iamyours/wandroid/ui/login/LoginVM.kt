package io.github.iamyours.wandroid.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel

class LoginVM : BaseViewModel() {
    val username = MutableLiveData<String>()
    val password = MutableLiveData<String>()
    private val login = Transformations.switchMap(refreshTrigger) {
        api.login(username.value ?: "", password.value ?: "")
    }
    val loginUser = Transformations.map(login) {
        loading.value = false
        it.data
    }

    val close = MutableLiveData<Boolean>()

    fun login() {
        refreshTrigger.value = true
        loading.value = true
    }

    fun closeAction() {
        close.value = true
    }
}