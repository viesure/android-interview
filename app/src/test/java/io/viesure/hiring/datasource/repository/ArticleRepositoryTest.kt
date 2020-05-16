package io.viesure.hiring.datasource.repository

import BaseUnitTest
import com.nhaarman.mockitokotlin2.*
import getOrAwaitValue
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.datasource.database.DatabaseException
import io.viesure.hiring.datasource.database.dao.ArticleDbDao
import io.viesure.hiring.datasource.database.entity.ArticleDbEntity
import io.viesure.hiring.datasource.network.api.ArticleApi
import io.viesure.hiring.di.DiConfig
import io.viesure.hiring.di.initKodein
import io.viesure.hiring.exception.ArticleNotFoundException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import newArticleDbEntity
import newArticleDto
import oldArticle
import oldArticleDbEntity
import oldArticleDto
import oldestArticle
import oldestArticleDbEntity
import oldestArticleDto
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.mockito.internal.verification.AtLeast


@ExperimentalCoroutinesApi
//TODO separate list and single article tests
class ArticleRepositoryTest : BaseUnitTest() {
    private lateinit var mockedApi : ArticleApi
    private lateinit var mockedDbDao : ArticleDbDao
    private lateinit var repositoryInTest: ArticleRepository

    @Before
    override fun before() {
        super.before()
        mockedApi = mock()
        mockedDbDao = mock()

        val kodein = Kodein{
            initKodein()

            bind<ArticleApi>(overrides = true) with provider { mockedApi }
            bind<ArticleDbDao>(overrides = true) with provider { mockedDbDao }
            bind<CoroutineDispatcher>(tag = DiConfig.TAG_IO_DISPATCHER, overrides = true) with provider {testDispatcher}
        }

        repositoryInTest = kodein.direct.instance()
    }

    @Test
    fun testLoadArticlesFromNetwork() = runBlockingTest {

        val articlesFromNetwork = listOf(oldArticleDto, newArticleDto, oldestArticleDto)
        val expectedList = listOf(oldArticle, newArticle, oldestArticle)
        val expectedToDb = listOf(oldArticleDbEntity, newArticleDbEntity, oldestArticleDbEntity)

        whenever(mockedApi.getArticles()).thenReturn(articlesFromNetwork)

        val articleListResult = repositoryInTest.getArticles()
        val singleArticleResult = repositoryInTest.getArticle(oldArticle.id)

        val dbArgumentCaptor = argumentCaptor<List<ArticleDbEntity>>()
        verify(mockedDbDao, AtLeast(1)).replaceAllArticles(dbArgumentCaptor.capture())

        assertEquals(Resource.Success(expectedList), articleListResult.getOrAwaitValue { })
        assertEquals(Resource.Success(oldArticle), singleArticleResult.getOrAwaitValue { })
        assertEquals(expectedToDb, dbArgumentCaptor.firstValue)
    }

    @Test
    fun testLoadArticlesDataBaseFallback() = runBlockingTest {
        val expectedList = listOf(newArticle, oldArticle, oldestArticle)
        val expectedFromDb = listOf(newArticleDbEntity, oldArticleDbEntity, oldestArticleDbEntity)

        whenever(mockedApi.getArticles()).thenThrow(RuntimeException())
        whenever(mockedDbDao.getArticlesInReleaseOrder()).thenReturn(expectedFromDb)

        val articleListResult = repositoryInTest.getArticles()
        val singleArticleResult = repositoryInTest.getArticle(oldArticle.id)

        assertEquals(Resource.Success(expectedList), articleListResult.getOrAwaitValue { })
        assertEquals(Resource.Success(oldArticle), singleArticleResult.getOrAwaitValue { })
    }

    @Test
    fun testErrorWithEmptyDb() = runBlockingTest {
        val exception = RuntimeException()
        whenever(mockedApi.getArticles()).thenThrow(exception)
        whenever(mockedDbDao.getArticlesInReleaseOrder()).thenReturn(listOf())

        val listResult = repositoryInTest.getArticles()
        val singleArticleResult = repositoryInTest.getArticle("whatewer")

        assertEquals(Resource.Error(exception), listResult.getOrAwaitValue { })
        assertEquals(Resource.Error(exception), singleArticleResult.getOrAwaitValue { })
    }

    @Test
    fun testErrorOnBothDataSource() = runBlockingTest {
        val networkException = RuntimeException("networkException")
        val databaseException = RuntimeException("databaseException")

        whenever(mockedApi.getArticles()).thenThrow(networkException)
        whenever(mockedDbDao.getArticlesInReleaseOrder()).thenThrow(databaseException)

        val listResult = repositoryInTest.getArticles()
        val singleArticleResult = repositoryInTest.getArticle("whatewer")

        assertEquals(databaseException, ((listResult.getOrAwaitValue { } as? Resource.Error)?.exception as? DatabaseException)?.cause)
        assertEquals(databaseException, ((singleArticleResult.getOrAwaitValue { } as? Resource.Error)?.exception as? DatabaseException)?.cause)
    }

    @Test
    fun testLoadArticleNotFound() = runBlockingTest {
        val articlesFromNetwork = listOf(oldArticleDto, newArticleDto)

        whenever(mockedApi.getArticles()).thenReturn(articlesFromNetwork)

        val singleArticleResult = repositoryInTest.getArticle(oldestArticleDto.id)

        assert((singleArticleResult.getOrAwaitValue {  } as? Resource.Error)?.exception is ArticleNotFoundException)
    }
}

