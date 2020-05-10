package io.viesure.hiring

import android.app.Application

class ViesureApplication : Application() {
    companion object {
        lateinit var application: Application
    }

    override fun onCreate() {
        super.onCreate()
        application = this
    }
}