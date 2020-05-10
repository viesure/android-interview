package io.viesure.hiring.datasource.database.mappers

import android.net.Uri
import io.viesure.hiring.datasource.database.entity.ArticleDbEntity
import io.viesure.hiring.model.Article
import java.util.*

fun ArticleDbEntity.toArticle() = Article(
    this.id,
    this.title ?: "",
    this.description,
    this.author,
    this.releaseDate?.let { Date(it) },
    this.image?.let { Uri.parse(it) }
)

fun List<ArticleDbEntity>.toArticleList() = this.map { it.toArticle() }

fun Article.toArticleDbEntity() = ArticleDbEntity(
    this.id,
    this.title,
    this.description,
    this.author,
    this.releaseDate?.time,
    this.image?.toString()
)

fun List<Article>.toArticleDbEntityList() = this.map { it.toArticleDbEntity() }