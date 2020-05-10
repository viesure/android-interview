package io.viesure.hiring.datasource.network.api

import io.viesure.hiring.datasource.network.dto.ArticleDto
import retrofit2.http.GET

interface ArticleApi {
    @GET("articles")
    suspend fun getArticles(): List<ArticleDto>
}