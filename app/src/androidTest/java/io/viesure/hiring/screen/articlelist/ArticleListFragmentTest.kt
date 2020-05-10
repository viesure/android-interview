package io.viesure.hiring.screen.articlelist

import BaseUnitTest
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.reset
import com.nhaarman.mockitokotlin2.whenever
import io.viesure.hiring.MainActivity
import io.viesure.hiring.R
import io.viesure.hiring.di.appKodein
import io.viesure.hiring.screen.articlelist.ArticleListViewModel
import io.viesure.hiring.screen.base.Event
import io.viesure.hiring.view.ArticleHeaderWidget
import io.viesure.hiring.view.ErrorWidget
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import newArticle
import oldArticle
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleListFragmentTest : BaseUnitTest() {
    private val mockedViewModel: ArticleListViewModel = mock()
    private val articleLiveData = MediatorLiveData<List<ArticleHeaderWidget.ViewContent>>()
    private val loadingLiveData = MediatorLiveData<Int>()
    private val navigationLiveData = MutableLiveData<Event<String>>()
    private val errorLiveData = MediatorLiveData<ErrorWidget.ErrorViewContent>()

    init {
        appKodein = Kodein {
            extend(appKodein, copy = Copy.All, allowOverride = true)
            bind<ArticleListViewModel>(overrides = true) with provider { mockedViewModel }
        }

        whenever(mockedViewModel.articleHeaders).thenReturn(articleLiveData)
        whenever(mockedViewModel.articleIdSelected).thenReturn(navigationLiveData)
        whenever(mockedViewModel.error).thenReturn(errorLiveData)
        whenever(mockedViewModel.loadingVisibility).thenReturn(loadingLiveData)
    }

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testLoad() = runBlockingTest {

        articleLiveData.value =
            listOf(
                ArticleHeaderWidget.ViewContent(newArticle) { navigationLiveData.value = Event(newArticle.id) },
                ArticleHeaderWidget.ViewContent(oldArticle, null)
            )

        Thread.sleep(200)

        onView(withText(newArticle.title)).check(matches(isDisplayed()))
        onView(withText(oldArticle.title)).check(matches(isDisplayed()))

        onView(withText(newArticle.title)).perform(click())
        //TODO check navigation
    }
}