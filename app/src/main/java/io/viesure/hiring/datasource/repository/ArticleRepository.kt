package io.viesure.hiring.datasource.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.database.DatabaseException
import io.viesure.hiring.datasource.database.dao.ArticleDbDao
import io.viesure.hiring.datasource.database.mappers.toArticleDbEntityList
import io.viesure.hiring.datasource.database.mappers.toArticleList
import io.viesure.hiring.datasource.network.api.ArticleApi
import io.viesure.hiring.datasource.network.mappers.toArticleList
import io.viesure.hiring.exception.ArticleNotFoundException
import io.viesure.hiring.model.Article
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

interface ArticleRepository {
    suspend fun getArticles(): LiveData<Resource<List<Article>>>
    suspend fun getArticle(articleId: String): LiveData<Resource<Article>>
}

class ArticleRepositoryImpl(
    private val articleApi: ArticleApi,
    private val articleDbDao: ArticleDbDao,
    private val ioDispatcher: CoroutineDispatcher
) : ArticleRepository {

    override suspend fun getArticles(): LiveData<Resource<List<Article>>> {

        val result = MutableLiveData<Resource<List<Article>>>()
        result.postValue(Resource.Loading)

        CoroutineScope(coroutineContext).launch(ioDispatcher) {
            result.postValue(loadArticles())
        }

        return result
    }

    override suspend fun getArticle(articleId: String): LiveData<Resource<Article>> {
        //TODO could be optimized to get only the requested article from the database
        val result = MutableLiveData<Resource<Article>>()
        result.postValue(Resource.Loading)

        CoroutineScope(coroutineContext).launch(ioDispatcher) {
            when (val articles = loadArticles()) {
                is Resource.Success -> {
                    val article = articles.data.firstOrNull { it.id == articleId }
                    if(article != null) {
                        result.postValue(Resource.Success(article))
                    }else {
                        result.postValue(Resource.Error(ArticleNotFoundException()))
                    }
                }
                is Resource.Loading -> result.postValue(Resource.Loading)
                is Resource.Error -> result.postValue(Resource.Error(articles.exception))
            }
        }

        return result
    }

    private suspend fun loadArticles(): Resource<List<Article>> {
        return try {
            val articlesFromNetwork = articleApi.getArticles().toArticleList()
            saveToDb(articlesFromNetwork)
            Resource.Success(articlesFromNetwork)
        } catch (networkException: Exception) {

            try {

                val articlesFromDb = articleDbDao.getArticlesInReleaseOrder()
                if (articlesFromDb.isNotEmpty()) {
                    Resource.Success(articlesFromDb.toArticleList())
                } else {
                    Resource.Error(networkException)
                }

            } catch (dbException: Exception) {
                Resource.Error(DatabaseException(dbException))
            }
        }
    }


    private fun saveToDb(articles: List<Article>) {
        GlobalScope.launch(ioDispatcher) {
            articleDbDao.replaceAllArticles(articles.toArticleDbEntityList())
        }
    }
}