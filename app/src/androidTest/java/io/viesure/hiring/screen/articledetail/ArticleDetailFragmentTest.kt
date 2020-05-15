package io.viesure.hiring.screen.articledetail

import BaseUnitTest
import android.os.Bundle
import android.text.SpannedString
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.MediatorLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.viesure.hiring.MainActivity
import io.viesure.hiring.ViesureApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class ArticleDetailFragmentTest : BaseUnitTest() {
    private lateinit var mockedViewModel: ArticleDetailViewModel
    private lateinit var liveData: MediatorLiveData<ArticleDetailViewContent>

    @Before
    override fun before() {
        super.before()
        mockedViewModel = mock()

        val app = (InstrumentationRegistry.getInstrumentation().targetContext.applicationContext as ViesureApplication)
        app.kodein = Kodein {
            extend(app.kodein, copy = Copy.All, allowOverride = true)

            bind<ArticleDetailViewModel>(overrides = true) with provider { mockedViewModel }
        }

        liveData = MediatorLiveData<ArticleDetailViewContent>()
        whenever(mockedViewModel.articleDetailViewContent).thenReturn(liveData)
        whenever(mockedViewModel.navigation).thenReturn(mock())
    }

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testLoad() {
        val articleId = "id"
        val articleTitle = "Article title"
        launchFragmentInContainer(
            Bundle().also { it.putString(ArticleDetailFragment.ARG_ARTICLE_ID, articleId) },
            0
        ) { ArticleDetailFragment() }
        liveData.value = ArticleDetailViewContent(articleTitle, "desc", SpannedString("auth"), "1234", null)
        verify(mockedViewModel).load(eq(articleId), eq(false))

        onView(withText(articleTitle)).check(matches(isDisplayed()))
    }
}