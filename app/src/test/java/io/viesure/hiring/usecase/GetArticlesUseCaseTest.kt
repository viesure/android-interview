package io.viesure.hiring.usecase

import BaseUnitTest
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import getOrAwaitValue
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.repository.ArticleRepository
import io.viesure.hiring.model.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import oldArticle
import oldestArticle
import org.junit.*
import org.junit.Assert.assertEquals
import java.lang.Exception

@ExperimentalCoroutinesApi
class GetArticlesUseCaseTest : BaseUnitTest() {

    @Test
    fun testSuccessAndSort() = runBlockingTest {

        val data = listOf(oldArticle, oldestArticle, newArticle)
        val expected = listOf(newArticle, oldArticle, oldestArticle)

        val liveData = MutableLiveData<Resource<List<Article>>>()
        liveData.value = Resource.Success(data)
        val repository = mock<ArticleRepository>()

        whenever(repository.getArticles()).thenReturn(liveData)

        val useCase = GetArticleListUseCaseImpl(repository, testDispatcher)

        val result = useCase.invoke().getOrAwaitValue()

        assertEquals(expected, (result as Resource.Success<List<Article>>).data)
    }

    @Test
    fun testLoadingThenError() = runBlockingTest {
        val liveData = MutableLiveData<Resource<List<Article>>>()
        liveData.value = Resource.Loading
        val repository = mock<ArticleRepository>()

        whenever(repository.getArticles()).thenReturn(liveData)

        val useCase = GetArticleListUseCaseImpl(repository, testDispatcher)

        val resultLiveData = useCase.invoke()
        assertEquals(Resource.Loading, resultLiveData.getOrAwaitValue())

        val error = Resource.Error(Exception())
        liveData.value = error
        assertEquals(error, resultLiveData.getOrAwaitValue())
    }
}