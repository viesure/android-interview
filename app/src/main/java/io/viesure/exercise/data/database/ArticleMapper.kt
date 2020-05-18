package io.viesure.exercise.data.database

import io.viesure.exercise.model.Article
import java.util.*


fun ArticleEntity.toArticle() = Article(
    this.id,
    this.author,
    this.description,
    this.image,
    this.releaseDate,
    this.title,
    this.dateForSorting?.let { Date(it) }
)

fun List<ArticleEntity>.toArticleList() = this.map { it.toArticle() }

fun Article.toArticleEntity() = ArticleEntity(
    this.id,
    this.author,
    this.description,
    this.image,
    this.releaseDate,
    this.title,
    this.dateForSorting?.time
)

fun List<Article>.toArticleEntityList() = this.map { it.toArticleEntity() }