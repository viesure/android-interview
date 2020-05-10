package io.viesure.hiring.datasource.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.viesure.hiring.datasource.database.dao.ArticleDbDao
import io.viesure.hiring.datasource.database.entity.ArticleDbEntity

@Database(entities = [ArticleDbEntity::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDbDao(): ArticleDbDao
}