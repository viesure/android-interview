package io.viesure.hiring.screen.articlelist

import BaseUnitTest
import android.text.Html
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import getOrAwaitValue
import io.viesure.hiring.R
import io.viesure.hiring.ViesureApplication
import io.viesure.hiring.datasource.Resource
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
import org.junit.Test
import java.io.IOException
import java.lang.RuntimeException
import java.text.SimpleDateFormat

@ExperimentalCoroutinesApi
class ArticleListViewModelTest: BaseUnitTest(){
    @Test
    fun testLoadingThenSuccess() = runBlockingTest {
        val mockedGetArticleListUseCase = mock<GetArticleListUseCase>()
        val sourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

        whenever(mockedGetArticleListUseCase.invoke()).thenReturn(sourceLiveData)

        val viewModel = ArticleListViewModel(ApplicationProvider.getApplicationContext<ViesureApplication>(), mockedGetArticleListUseCase)
        viewModel.load()

        sourceLiveData.value = Resource.Loading

        assertEquals(null, viewModel.articleHeaders.getOrAwaitValue { })
        assertEquals(null, viewModel.error.getOrAwaitValue { })
        assertEquals(View.VISIBLE, viewModel.loadingVisibility.getOrAwaitValue { })

        val sourceArticles = listOf(newArticle, oldArticle, oldestArticle)

        val expectedArticleHeaders = listOf(ArticleHeaderWidget.ViewContent(newArticle, null),
            ArticleHeaderWidget.ViewContent(oldArticle, null),
            ArticleHeaderWidget.ViewContent(oldestArticle, null))

        sourceLiveData.value = Resource.Success(sourceArticles)

        assertEquals(expectedArticleHeaders, viewModel.articleHeaders.getOrAwaitValue {  })
        assertEquals(null, viewModel.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })
    }

    @Test
    fun testError() = runBlockingTest{
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val mockedGetArticleListUseCase = mock<GetArticleListUseCase>()
        val sourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

        whenever(mockedGetArticleListUseCase.invoke()).thenReturn(sourceLiveData)

        val viewModel = ArticleListViewModel(ApplicationProvider.getApplicationContext<ViesureApplication>(), mockedGetArticleListUseCase)

        viewModel.load()

        sourceLiveData.value = Resource.Error(IOException())

        val expectedErrorViewContent = ErrorWidget.ErrorViewContent(
            context.getString(R.string.no_internet_title),
            context.getString(R.string.no_internet_description),
            true,
            null
        )

        val actualErrorViewContent = viewModel.error.getOrAwaitValue { }

        assertEquals(expectedErrorViewContent.title, actualErrorViewContent?.title)
        assertEquals(expectedErrorViewContent.message, actualErrorViewContent?.message)
        assertEquals(expectedErrorViewContent.retriable, actualErrorViewContent?.retriable)
        assertEquals(null, viewModel.articleHeaders.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })
    }

    @Test
    fun testRetry() = runBlockingTest {
        val mockedGetArticleListUseCase = mock<GetArticleListUseCase>()
        val failureSourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()
        val successSourceLiveData: MutableLiveData<Resource<List<Article>>> = MutableLiveData()

        whenever(mockedGetArticleListUseCase.invoke())
            .thenReturn(failureSourceLiveData)
            .thenReturn(successSourceLiveData)

        val viewModel = ArticleListViewModel(ApplicationProvider.getApplicationContext<ViesureApplication>(), mockedGetArticleListUseCase)

        failureSourceLiveData.value = Resource.Error(RuntimeException())

        viewModel.load()

        assertEquals(null, viewModel.articleHeaders.getOrAwaitValue { })
        assertNotNull(viewModel.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })

        val sourceArticles = listOf(newArticle, oldArticle, oldestArticle)

        val expectedArticleHeaders = listOf(ArticleHeaderWidget.ViewContent(newArticle, null),
            ArticleHeaderWidget.ViewContent(oldArticle, null),
            ArticleHeaderWidget.ViewContent(oldestArticle, null))

        viewModel.error.value?.onRetry?.invoke()

        successSourceLiveData.value = Resource.Success(sourceArticles)

        assertEquals(expectedArticleHeaders, viewModel.articleHeaders.getOrAwaitValue {  })
        assertEquals(null, viewModel.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })
    }
}