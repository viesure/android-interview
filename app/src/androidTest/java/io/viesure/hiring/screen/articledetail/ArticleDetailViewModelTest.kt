package io.viesure.hiring.screen.articledetail

import BaseUnitTest
import android.app.Application
import android.text.Html
import android.text.SpannedString
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import getOrAwaitValue
import io.viesure.hiring.R
import io.viesure.hiring.ViesureApplication
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.di.initKodein
import io.viesure.hiring.model.Article
import io.viesure.hiring.usecase.GetArticleUseCase
import io.viesure.hiring.view.ErrorWidget
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.io.IOException
import java.lang.Exception
import java.lang.RuntimeException
import java.text.SimpleDateFormat

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleDetailViewModelTest() : BaseUnitTest() {
    @Test
    fun testLoadingThenSuccess() = runBlockingTest {
        val articleId = "articleId"
        val mockedGetArticleUseCase = mock<GetArticleUseCase>()
        val sourceLiveData: MutableLiveData<Resource<Article>> = MutableLiveData()

        whenever(mockedGetArticleUseCase.invoke(eq(articleId))).thenReturn(sourceLiveData)

        val kodein = Kodein{
            initKodein()
            bind<GetArticleUseCase>(overrides = true) with provider {mockedGetArticleUseCase}
            bind<Application>() with singleton { ApplicationProvider.getApplicationContext<ViesureApplication>() }
        }

        val viewModel = kodein.direct.instance<ArticleDetailViewModel>()
        viewModel.load(articleId)

        sourceLiveData.value = Resource.Loading

        assertEquals(null, viewModel.articleDetailViewContent.getOrAwaitValue { })
        assertEquals(null, viewModel.error.getOrAwaitValue { })
        assertEquals(View.VISIBLE, viewModel.loadingVisibility.getOrAwaitValue { })

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val expectedArticleDetailViewContent = ArticleDetailViewContent(
            newArticle.title,
            newArticle.description ?: "",
            Html.fromHtml(context.getString(R.string.article_details_author, newArticle.author), Html.FROM_HTML_MODE_LEGACY),
            newArticle.releaseDate?.let { SimpleDateFormat("EEE, MMM d, ''yy").format(it) } ?: "",
            newArticle.image
        )
        sourceLiveData.value = Resource.Success(newArticle)

        val actualDetailViewContent = viewModel.articleDetailViewContent.getOrAwaitValue { }
        assertEquals(expectedArticleDetailViewContent, actualDetailViewContent)
        assertEquals(null, viewModel.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })
    }

    @Test
    fun testError() = runBlockingTest{
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val mockedGetArticlesUseCase = mock<GetArticleUseCase>()
        val sourceLiveData: MutableLiveData<Resource<Article>> = MutableLiveData()

        whenever(mockedGetArticlesUseCase.invoke(any())).thenReturn(sourceLiveData)

        val viewModel = ArticleDetailViewModel(mockedGetArticlesUseCase, ApplicationProvider.getApplicationContext<ViesureApplication>())
        viewModel.load("whatever")

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
        assertEquals(null, viewModel.articleDetailViewContent.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })
    }

    @Test
    fun testRetry() = runBlockingTest {
        val mockedGetArticlesUseCase = mock<GetArticleUseCase>()
        val failureSourceLiveData: MutableLiveData<Resource<Article>> = MutableLiveData()
        val successSourceLiveData: MutableLiveData<Resource<Article>> = MutableLiveData()

        whenever(mockedGetArticlesUseCase.invoke(any()))
            .thenReturn(failureSourceLiveData)
            .thenReturn(successSourceLiveData)

        val viewModel = ArticleDetailViewModel(mockedGetArticlesUseCase, ApplicationProvider.getApplicationContext<ViesureApplication>())
        viewModel.load("whatever")

        failureSourceLiveData.value = Resource.Error(RuntimeException())

        assertEquals(null, viewModel.articleDetailViewContent.getOrAwaitValue { })
        assertNotNull(viewModel.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })

        val context = InstrumentationRegistry.getInstrumentation().targetContext

        val expectedArticleDetailViewContent = ArticleDetailViewContent(
            newArticle.title,
            newArticle.description ?: "",
            Html.fromHtml(context.getString(R.string.article_details_author, newArticle.author), Html.FROM_HTML_MODE_LEGACY),
            newArticle.releaseDate?.let { SimpleDateFormat("EEE, MMM d, ''yy").format(it) } ?: "",
            newArticle.image
        )

        viewModel.error.value?.onRetry?.invoke()

        successSourceLiveData.value = Resource.Success(newArticle)

        val actualDetailViewContent = viewModel.articleDetailViewContent.getOrAwaitValue { }
        assertEquals(expectedArticleDetailViewContent, actualDetailViewContent)
        assertEquals(null, viewModel.error.getOrAwaitValue { })
        assertEquals(View.GONE, viewModel.loadingVisibility.getOrAwaitValue { })
    }

}