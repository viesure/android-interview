package io.viesure.exercise.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.viesure.exercise.internal.DATABASE_NAME


@Database(entities = [ArticleEntity::class], version = 1)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile // All threads have immediate access to this property
        private var instance: ArticleDatabase? = null

        private val LOCK = Any() // Makes sure no threads making the same thing at the same time

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                DATABASE_NAME
            ).build()
    }
}