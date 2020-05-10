package io.viesure.hiring.usecase

import androidx.lifecycle.*
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.repository.ArticleRepository
import io.viesure.hiring.model.Article
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface GetArticleListUseCase{
    suspend operator fun invoke(): LiveData<Resource<List<Article>>>
}

class GetArticleListUseCaseImpl(private val articleRepository: ArticleRepository, private val defaultDispatcher: CoroutineDispatcher) : GetArticleListUseCase {
    override suspend operator fun invoke(): LiveData<Resource<List<Article>>> {

        return articleRepository.getArticles().switchMap {
            liveData(defaultDispatcher) {
                when (it) {
                    is Resource.Success -> emit(Resource.Success(it.data.sortedByDescending { it.releaseDate }))
                    else -> emit(it)
                }
            }
        }
    }
}