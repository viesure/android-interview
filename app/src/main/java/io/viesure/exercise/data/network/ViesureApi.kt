package io.viesure.exercise.data.network

import io.reactivex.Single
import io.viesure.exercise.model.ArticlesResponse
import retrofit2.http.GET

interface ViesureApi {

    @GET("articles")
    fun getArticles(): Single<ArticlesResponse>
}