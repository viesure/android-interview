package io.viesure.hiring.screen.articlelist

import BaseUnitTest
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.viesure.hiring.MainActivity
import io.viesure.hiring.ViesureApplication
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
    private lateinit var mockedViewModel: ArticleListViewModel
    private lateinit var articleLiveData: MediatorLiveData<List<ArticleHeaderWidget.ViewContent>>
    private lateinit var loadingLiveData: MediatorLiveData<Int>
    private lateinit var navigationLiveData: MutableLiveData<Event<String>>
    private lateinit var errorLiveData: MediatorLiveData<ErrorWidget.ErrorViewContent>

    @Before
    override fun before() {
        super.before()

        mockedViewModel = mock()

        val app = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ViesureApplication)
        app.kodein = Kodein {
            extend(app.kodein, copy = Copy.All, allowOverride = true)

            bind<ArticleListViewModel>(overrides = true) with provider { mockedViewModel }
        }


        articleLiveData = MediatorLiveData<List<ArticleHeaderWidget.ViewContent>>()
        loadingLiveData = MediatorLiveData<Int>()
        navigationLiveData = MutableLiveData<Event<String>>()
        errorLiveData = MediatorLiveData<ErrorWidget.ErrorViewContent>()

        whenever(mockedViewModel.articleHeaders).thenReturn(articleLiveData)
        whenever(mockedViewModel.articleIdSelected).thenReturn(navigationLiveData)
        whenever(mockedViewModel.error).thenReturn(errorLiveData)
        whenever(mockedViewModel.loadingVisibility).thenReturn(loadingLiveData)
    }

    @Test
    fun testLoad() = runBlockingTest {

        val scenario = launchActivity<MainActivity>()

        articleLiveData.value =
            listOf(
                ArticleHeaderWidget.ViewContent(newArticle) { navigationLiveData.value = Event(newArticle.id) },
                ArticleHeaderWidget.ViewContent(oldArticle, null)
            )

        //TODO test failed sometimes without waiting here,but that was before using activityScenario, after some more testing it may be removed
        Thread.sleep(200)

        onView(withText(newArticle.title)).check(matches(isDisplayed()))
        onView(withText(oldArticle.title)).check(matches(isDisplayed()))

        onView(withText(newArticle.title)).perform(click())
        //TODO test navigation

        scenario.close()
    }
}