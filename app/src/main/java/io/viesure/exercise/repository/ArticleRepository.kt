package io.viesure.exercise.repository

import io.reactivex.disposables.Disposable

interface ArticleRepository {

    fun getArticleList(): Disposable

    fun insertArticles(): Disposable
}