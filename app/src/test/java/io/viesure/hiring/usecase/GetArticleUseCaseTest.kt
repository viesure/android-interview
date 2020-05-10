package io.viesure.hiring.usecase

import BaseUnitTest
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import getOrAwaitValue
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.repository.ArticleRepository
import io.viesure.hiring.model.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception

@ExperimentalCoroutinesApi
class GetArticleUseCaseTest : BaseUnitTest() {

    @Test
    fun testSuccess() = runBlockingTest {
        val liveData = MutableLiveData<Resource<Article>>()
        liveData.value = Resource.Success(newArticle)
        val repository = mock<ArticleRepository>()

        whenever(repository.getArticle(any())).thenReturn(liveData)

        val useCase = GetArticleUseCaseImpl(repository)

        assertEquals(Resource.Success(newArticle), useCase.invoke("whatever").getOrAwaitValue())
    }

    @Test
    fun testLoadingThenError() = runBlockingTest {
        val liveData = MutableLiveData<Resource<Article>>()
        liveData.value = Resource.Loading
        val repository = mock<ArticleRepository>()

        whenever(repository.getArticle(any())).thenReturn(liveData)

        val useCase = GetArticleUseCaseImpl(repository)

        val resultLiveData = useCase.invoke("whatever")
        assertEquals(Resource.Loading, resultLiveData.getOrAwaitValue())

        val error = Resource.Error(Exception())
        liveData.value = error
        assertEquals(error, resultLiveData.getOrAwaitValue())
    }
}