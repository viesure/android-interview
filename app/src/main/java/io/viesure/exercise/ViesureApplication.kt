package io.viesure.exercise

import android.app.Application
import androidx.room.Room
import io.viesure.exercise.data.database.ArticleDatabase
import io.viesure.exercise.internal.DATABASE_NAME

class ViesureApplication : Application() {

    companion object {
        lateinit var database: ArticleDatabase
        lateinit var INSTANCE: ViesureApplication
        var isDatabaseEmpty = true
    }

    init {
        INSTANCE = this
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        database = Room.databaseBuilder(
            applicationContext,
            ArticleDatabase::class.java,
            DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }
}