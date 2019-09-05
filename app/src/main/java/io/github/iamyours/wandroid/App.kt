package io.github.iamyours.wandroid

import android.app.Application
import io.github.iamyours.wandroid.db.AppDataBase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        AppDataBase.init(this)
    }

    companion object {
        lateinit var instance: Application
    }
}