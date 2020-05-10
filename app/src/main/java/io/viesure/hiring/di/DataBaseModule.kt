package io.viesure.hiring.di

import android.app.Application
import androidx.room.Room
import io.viesure.hiring.datasource.database.ArticleDatabase
import io.viesure.hiring.datasource.database.dao.ArticleDbDao
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val databaseModule = Kodein.Module(DiConfig.MODULE_NAME_DATABASE) {
    bind<ArticleDatabase>() with singleton{
        Room.databaseBuilder(
            instance<Application>().applicationContext,
            ArticleDatabase::class.java,
            "article_database"
        ).build()
    }

    bind<ArticleDbDao>() with provider {
        instance<ArticleDatabase>().articleDbDao()
    }
}