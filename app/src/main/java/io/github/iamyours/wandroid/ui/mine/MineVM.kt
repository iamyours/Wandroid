package io.github.iamyours.wandroid.ui.mine

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import io.github.iamyours.wandroid.base.BaseViewModel
import io.github.iamyours.wandroid.util.AbsentLiveData
import io.github.iamyours.wandroid.util.LiveDataBus
import io.github.iamyours.wandroid.util.SP

class MineVM : BaseViewModel() {
    //跳转登录
    val toLogin = MutableLiveData<Boolean>()
    //跳转收藏
    val toCollect = MutableLiveData<Boolean>()
    //跳转阅读历史
    val toHistory = MutableLiveData<Boolean>()

    val isLogin = MutableLiveData<Boolean>()

    val name = Transformations.map(isLogin) {
        val username = SP.getString(SP.KEY_USER_NAME)
        val nickname = SP.getString(SP.KEY_NICK_NAME)
        if (TextUtils.isEmpty(nickname)) username else nickname
    }

    private val _userInfo = Transformations.switchMap(isLogin) {
        if (it) {
            api.userInfo()
        } else {
            AbsentLiveData.create()
        }
    }

    val userInfo = Transformations.map(_userInfo) {
        loading.value = false
        it.data
    }


    init {
        isLogin.value = SP.getBoolean(SP.KEY_IS_LOGIN)
        isLogin.observeForever {
            //登录成功，加载进度获取个人信息
            if (it) loading.value = true
        }
    }

    fun isNotLogin(): Boolean {
        return if (isLogin.value == true) false
        else {
            toLogin.value = true
            true
        }
    }

    fun login() {
        isNotLogin()
    }

    fun logout() {
        SP.logout()
        isLogin.value = false
        LiveDataBus.username.postValue("")
    }

    fun toCollectAction() {
        if (isNotLogin()) return
        toCollect.value = true
    }

    fun toHistoryAction() {
        if (isNotLogin()) return
        toHistory.value = true
    }
}