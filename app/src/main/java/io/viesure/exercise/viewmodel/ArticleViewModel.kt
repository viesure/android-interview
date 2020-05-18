package io.viesure.exercise.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.viesure.exercise.di.DaggerAppComponent
import io.viesure.exercise.repository.ArticleRepositoryImpl
import javax.inject.Inject

class ArticleViewModel : ViewModel() {

    @Inject
    lateinit var repository: ArticleRepositoryImpl

    private val compositeDisposable by lazy { CompositeDisposable() }


    init {
        DaggerAppComponent.create().inject(this)
        compositeDisposable.add(repository.insertArticles())
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun fetchForArticles() {
        compositeDisposable.add(repository.getArticleList())
    }
}