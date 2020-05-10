package io.viesure.hiring.screen.articledetail

import BaseUnitTest
import android.os.Bundle
import android.text.SpannedString
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MediatorLiveData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.viesure.hiring.MainActivity
import io.viesure.hiring.di.appKodein
import kotlinx.coroutines.ExperimentalCoroutinesApi
import newArticle
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ArticleDetailFragmentTest : BaseUnitTest(){
    private val mockedViewModel = mock<ArticleDetailViewModel>()
    private val liveData = MediatorLiveData<ArticleDetailViewContent>()

    init{
        appKodein = Kodein {
            extend(appKodein, copy = Copy.All, allowOverride = true)

            bind<ArticleDetailViewModel>(overrides = true) with provider { mockedViewModel }
        }
        whenever(mockedViewModel.articleDetailViewContent).thenReturn(liveData)
    }

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun testLoad() {
        val articleId= "id"
        val articleTitle = "Article title"
        launchFragmentInContainer (
            Bundle().also{it.putString(ArticleDetailFragment.ARG_ARTICLE_ID, articleId)},
            0
        ) {ArticleDetailFragment()}
        liveData.value = ArticleDetailViewContent(articleTitle, "desc", SpannedString("auth"), "1234", null)
        verify(mockedViewModel).load(eq(articleId), eq(false))

        onView(withText(articleTitle)).check(matches(isDisplayed()))
    }
}