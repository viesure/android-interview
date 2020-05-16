package io.viesure.hiring.usecase

import BaseUnitTest
import androidx.lifecycle.MutableLiveData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import getOrAwaitValue
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.repository.ArticleRepository
import io.viesure.hiring.di.initKodein
import io.viesure.hiring.model.Article
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.lang.Exception

@ExperimentalCoroutinesApi
class GetArticleUseCaseTest : BaseUnitTest() {
    private lateinit var mockedArticleRepository: ArticleRepository
    private lateinit var useCaseInTest: GetArticleUseCase

    @Before
    override fun before(){
        super.before()
        mockedArticleRepository = mock()
        val kodein = Kodein{
            initKodein()
            bind<ArticleRepository>(overrides = true) with provider { mockedArticleRepository }
        }
        useCaseInTest = kodein.direct.instance()
    }

    @Test
    fun testSuccess() = runBlockingTest {
        val liveData = MutableLiveData<Resource<Article>>()
        liveData.value = Resource.Success(newArticle)

        whenever(mockedArticleRepository.getArticle(eq(newArticle.id))).thenReturn(liveData)

        assertEquals(Resource.Success(newArticle), useCaseInTest.invoke(newArticle.id).getOrAwaitValue())
    }

    @Test
    fun testLoadingThenError() = runBlockingTest {
        val liveData = MutableLiveData<Resource<Article>>()
        liveData.value = Resource.Loading

        whenever(mockedArticleRepository.getArticle(newArticle.id)).thenReturn(liveData)

        val resultLiveData = useCaseInTest.invoke(newArticle.id)
        assertEquals(Resource.Loading, resultLiveData.getOrAwaitValue())

        val error = Resource.Error(Exception())
        liveData.value = error
        assertEquals(error, resultLiveData.getOrAwaitValue())
    }
}