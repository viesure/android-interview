package io.viesure.hiring.usecase

import androidx.lifecycle.LiveData
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.repository.ArticleRepository
import io.viesure.hiring.model.Article

interface GetArticleUseCase {
    suspend operator fun invoke(articleId: String): LiveData<Resource<Article>>
}

class GetArticleUseCaseImpl(private val articleRepository: ArticleRepository) : GetArticleUseCase {
    override suspend operator fun invoke(articleId: String) = articleRepository.getArticle(articleId)
}