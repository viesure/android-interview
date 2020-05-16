package io.viesure.hiring.usecase

import BaseUnitTest
import androidx.lifecycle.MutableLiveData
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
import oldArticle
import oldestArticle
import org.junit.*
import org.junit.Assert.assertEquals
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import java.lang.Exception

@ExperimentalCoroutinesApi
class GetArticleListUseCaseTest : BaseUnitTest() {
    private lateinit var mockedArticleRepository: ArticleRepository
    private lateinit var useCaseInTest: GetArticleListUseCase

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
    fun testSuccessAndSort() = runBlockingTest {

        val data = listOf(oldArticle, oldestArticle, newArticle)
        val expected = listOf(newArticle, oldArticle, oldestArticle)

        val liveData = MutableLiveData<Resource<List<Article>>>()
        liveData.value = Resource.Success(data)

        whenever(mockedArticleRepository.getArticles()).thenReturn(liveData)

        val result = useCaseInTest.invoke().getOrAwaitValue()

        assertEquals(expected, (result as Resource.Success<List<Article>>).data)
    }

    @Test
    fun testLoadingThenError() = runBlockingTest {
        val liveData = MutableLiveData<Resource<List<Article>>>()
        liveData.value = Resource.Loading

        whenever(mockedArticleRepository.getArticles()).thenReturn(liveData)

        val resultLiveData = useCaseInTest.invoke()
        assertEquals(Resource.Loading, resultLiveData.getOrAwaitValue())

        val error = Resource.Error(Exception())
        liveData.value = error
        assertEquals(error, resultLiveData.getOrAwaitValue())
    }
}