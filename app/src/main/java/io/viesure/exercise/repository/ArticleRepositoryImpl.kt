package io.viesure.exercise.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.viesure.exercise.ViesureApplication
import io.viesure.exercise.data.database.toArticleEntityList
import io.viesure.exercise.data.database.toArticleList
import io.viesure.exercise.data.network.ViesureApi
import io.viesure.exercise.di.DaggerAppComponent
import io.viesure.exercise.internal.ARTICLE_OBSERVER_TAG
import io.viesure.exercise.internal.INSERT_ARTICLES_METHOD
import io.viesure.exercise.model.Article
import io.viesure.exercise.model.ArticlesResponse
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ArticleRepositoryImpl : ArticleRepository {

    @Inject
    lateinit var viesureApiService: ViesureApi


    init {
        DaggerAppComponent.create().inject(this)
    }

    private val articleList by lazy { MutableLiveData<List<Article>>() }
    val articleListLD: LiveData<List<Article>>
        get() = articleList

    private val isInProgress by lazy { MutableLiveData<Boolean>() }
    val isInProgressLD: LiveData<Boolean>
        get() = isInProgress

    private val isError by lazy { MutableLiveData<Boolean>() }
    val isErrorLD: LiveData<Boolean>
        get() = isError


    override fun insertArticles(): Disposable {
        return viesureApiService.getArticles()
            .subscribeOn(Schedulers.io())
            .subscribe(
                { articlesResponse ->
                    if (ViesureApplication.isDatabaseEmpty) {
                        if (articlesResponse != null) {
                            val tempArticlesResponse =
                                assignValuesForSortingProperty(articlesResponse)
                            val entityList = tempArticlesResponse.toList().toArticleEntityList()
                            ViesureApplication.database.apply {
                                articleDao().insertArticles(
                                    entityList
                                )
                            }
                        }
                        ViesureApplication.isDatabaseEmpty = false
                        getArticleList()
                    }
                },
                {
                    isInProgress.postValue(true)
                    Log.e(INSERT_ARTICLES_METHOD, "ArticlesResponse error: ${it.message}")
                    isError.postValue(true)
                    isInProgress.postValue(false)
                }
            )
    }

    override fun getArticleList(): Disposable {
        return getArticlesQuery()
    }

    private fun getArticlesQuery(): Disposable {
        return ViesureApplication.database.articleDao()
            .fetchArticlesByReleaseDate()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { articleEntityList ->
                    isInProgress.postValue(true)
                    if (articleEntityList != null) {
                        isError.postValue(false)
                        articleList.postValue(articleEntityList.toArticleList())
                    } else {
                        isError.postValue(true)
                    }
                    isInProgress.postValue(false)
                },
                {
                    isInProgress.postValue(true)
                    articleList.postValue(emptyList())
                    Log.e(ARTICLE_OBSERVER_TAG, "ArticlesResponse error: ${it.message}")
                    isError.postValue(true)
                    isInProgress.postValue(false)
                }
            )
    }

    private fun formatStringToDate(responseDate: String): Date {
        val originalFormat: DateFormat = SimpleDateFormat("M/d/yyyy", Locale.ENGLISH)
        return originalFormat.parse(responseDate)
    }

    private fun assignValuesForSortingProperty(articlesResponse: ArticlesResponse): ArticlesResponse {
        articlesResponse.forEach { article ->
            article.dateForSorting = formatStringToDate(article.releaseDate)
        }
        return articlesResponse
    }
}