package io.viesure.hiring.screen.articlelist

import BaseUnitTest
import android.app.Application
import android.content.Context
import android.text.Html
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import getOrAwaitValue
import io.viesure.hiring.R
import io.viesure.hiring.ViesureApplication
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.di.initKodein
import io.viesure.hiring.model.Article
import io.viesure.hiring.screen.articledetail.ArticleDetailViewContent
import io.viesure.hiring.screen.articledetail.ArticleDetailViewModel
import io.viesure.hiring.usecase.GetArticleListUseCase
import io.viesure.hiring.usecase.GetArticleUseCase
import io.viesure.hiring.view.ArticleHeaderWidget
import io.viesure.hiring.view.ErrorWidget
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import oldArticle
import oldestArticle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.IOException
import java.lang.RuntimeException
import java.text.SimpleDateFormat

@ExperimentalCoroutinesApi
class ArticleListViewModelTest: BaseUnitTest(){
    private lateinit var mockedGetArticleListUseCase: GetArticleListUseCase
    private lateinit var viewModelInTest: ArticleListViewModel
    private lateinit var context: Context

    @Before
    override fun before() {
        super.before()
        mockedGetArticleListUseCase = mock()
        context = InstrumentationRegistry.getInstrumentation().targetContext

        val kodein = Kodein{
            initKodein()
            bind<GetArticleListUseCase>(overrides = true) with provider {mockedGetArticleListUseCase}
            bind<Application>() with singleton { ApplicationProvider.getApplicationContext<ViesureApplication>() }
        }

        viewModelInTest = kodein.direct.instance()
    }

    @Test
    fun testLoadingThenSuccess() = runBlockingTest {
        val sourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

        whenever(mockedGetArticleListUseCase.invoke()).thenReturn(sourceLiveData)

        viewModelInTest.load()

        sourceLiveData.value = Resource.Loading

        assertEquals(null, viewModelInTest.articleHeaders.getOrAwaitValue { })
        assertEquals(null, viewModelInTest.error.getOrAwaitValue { })
        assertEquals(View.VISIBLE, viewModelInTest.loadingVisibility.getOrAwaitValue { })

        val sourceArticles = listOf(newArticle, oldArticle, oldestArticle)

        val expectedArticleHeaders = listOf(ArticleHeaderWidget.ViewContent(newArticle, null),
            ArticleHeaderWidget.ViewContent(oldArticle, null),
            ArticleHeaderWidget.ViewContent(oldestArticle, null))

        sourceLiveData.value = Resource.Success(sourceArticles)

        assertEquals(expectedArticleHeaders, viewModelInTest.articleHeaders.getOrAwaitValue {  })
        assertEquals(null, viewModelInTest.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModelInTest.loadingVisibility.getOrAwaitValue { })
    }

    @Test
    fun testError() = runBlockingTest{
        val sourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

        whenever(mockedGetArticleListUseCase.invoke()).thenReturn(sourceLiveData)

        viewModelInTest.load()

        sourceLiveData.value = Resource.Error(JsonEncodingException(""))

        val expectedErrorViewContent = ErrorWidget.ErrorViewContent(
            context.getString(R.string.json_parse_error_title),
            context.getString(R.string.json_parse_error_description),
            true,
            null
        )

        val actualErrorViewContent = viewModelInTest.error.getOrAwaitValue { }

        assertEquals(expectedErrorViewContent.title, actualErrorViewContent?.title)
        assertEquals(expectedErrorViewContent.message, actualErrorViewContent?.message)
        assertEquals(expectedErrorViewContent.retriable, actualErrorViewContent?.retriable)
        assertEquals(null, viewModelInTest.articleHeaders.getOrAwaitValue { })
        assertEquals(View.GONE, viewModelInTest.loadingVisibility.getOrAwaitValue { })
    }

    @Test
    fun testRetry() = runBlockingTest {
        val failureSourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()
        val successSourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

        whenever(mockedGetArticleListUseCase.invoke())
            .thenReturn(failureSourceLiveData)
            .thenReturn(successSourceLiveData)

        failureSourceLiveData.value = Resource.Error(JsonDataException ())

        viewModelInTest.load()

        assertEquals(null, viewModelInTest.articleHeaders.getOrAwaitValue { })
        assertNotNull(viewModelInTest.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModelInTest.loadingVisibility.getOrAwaitValue { })

        val sourceArticles = listOf(newArticle, oldArticle, oldestArticle)

        val expectedArticleHeaders = listOf(ArticleHeaderWidget.ViewContent(newArticle, null),
            ArticleHeaderWidget.ViewContent(oldArticle, null),
            ArticleHeaderWidget.ViewContent(oldestArticle, null))

        viewModelInTest.error.value?.onRetry?.invoke()

        successSourceLiveData.value = Resource.Success(sourceArticles)

        assertEquals(expectedArticleHeaders, viewModelInTest.articleHeaders.getOrAwaitValue {  })
        assertEquals(null, viewModelInTest.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModelInTest.loadingVisibility.getOrAwaitValue { })
    }
}