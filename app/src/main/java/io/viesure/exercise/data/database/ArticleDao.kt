package io.viesure.exercise.data.database

import androidx.room.*
import io.reactivex.Single


@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticles(articles: List<ArticleEntity>)

    @Query("SELECT * from article ORDER BY date_for_sorting DESC")
    fun fetchArticlesByReleaseDate(): Single<List<ArticleEntity>>
}