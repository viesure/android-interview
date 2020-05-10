package io.viesure.hiring.datasource.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import io.viesure.hiring.datasource.database.entity.ArticleDbEntity
import java.nio.file.Files.delete


@Dao
interface ArticleDbDao {
    @Query("SELECT * from articles ORDER BY releaseDate ASC")
    suspend fun getArticlesInReleaseOrder(): List<ArticleDbEntity>

    @Insert
    suspend fun insertArticles(articles: List<ArticleDbEntity>)

    @Query("DELETE FROM articles")
    suspend fun deleteAll()

    @Transaction
    suspend fun replaceAllArticles(articles: List<ArticleDbEntity>) {
        deleteAll()
        insertArticles(articles)
    }
}