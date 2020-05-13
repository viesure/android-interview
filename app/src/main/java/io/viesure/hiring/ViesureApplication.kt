package io.viesure.hiring

import android.app.Application
import io.viesure.hiring.di.initKodein
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class ViesureApplication : Application(), KodeinAware {
    override lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()
        kodein = Kodein {
            initKodein()
            bind<Application>() with singleton { this@ViesureApplication }
        }
    }
}