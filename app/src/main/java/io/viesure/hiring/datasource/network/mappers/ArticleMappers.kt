package io.viesure.hiring.datasource.network.mappers

import android.net.Uri
import io.viesure.hiring.datasource.network.dto.ArticleDto
import io.viesure.hiring.model.Article

fun ArticleDto.toArticle() = Article(
    this.id,
    this.title ?: "",
    this.description,
    this.author,
    this.release_date,
    try {
        Uri.parse(this.image).buildUpon().scheme("https").build()
    }catch (e: Exception){
        null
    }
)

fun List<ArticleDto>.toArticleList() = this.map { it.toArticle() }