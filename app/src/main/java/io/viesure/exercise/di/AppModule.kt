package io.viesure.exercise.di

import dagger.Module
import dagger.Provides
import io.viesure.exercise.data.network.ViesureApi
import io.viesure.exercise.data.network.ViesureApiService
import io.viesure.exercise.model.Article
import io.viesure.exercise.repository.ArticleRepositoryImpl
import io.viesure.exercise.view.adapter.ArticleAdapter
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun provideApi(): ViesureApi = ViesureApiService.getClient()

    @Provides
    fun provideArticleList(): ArrayList<Article> = ArrayList()

    @Provides
    fun provideArticleAdapter(articles: ArrayList<Article>): ArticleAdapter {
        return ArticleAdapter(articles)
    }

    @Provides
    fun provideArticleRepository(): ArticleRepositoryImpl = ArticleRepositoryImpl()
}