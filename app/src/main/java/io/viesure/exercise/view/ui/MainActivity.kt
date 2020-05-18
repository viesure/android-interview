package io.viesure.exercise.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import io.viesure.exercise.R
import io.viesure.exercise.ViesureApplication
import io.viesure.exercise.di.DaggerAppComponent
import io.viesure.exercise.view.adapter.ArticleAdapter
import io.viesure.exercise.viewmodel.ArticleViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var articleAdapter: ArticleAdapter

    private val articleViewModel: ArticleViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        DaggerAppComponent.create().inject(this)

        if (!ViesureApplication.isDatabaseEmpty) {
            articleViewModel.fetchForArticles()
        }

        setUpRecyclerView()

        observeLiveData()
    }

    override fun onResume() {
        super.onResume()
        if (ViesureApplication.isDatabaseEmpty) {
            articleViewModel.fetchForArticles()
        }
    }

    /*=============================================================================
     |  Set up properties to display data
     *===========================================================================*/

    private fun setUpRecyclerView() {
        recycler_view.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = articleAdapter
        }
    }

    private fun observeLiveData() {
        observeInProgress()
        observeIsError()
        observeArticleList()
    }

    private fun observeInProgress() {
        articleViewModel.repository.isInProgressLD.observe(this, Observer { isLoading ->
            isLoading.let {
                if (it) {
                    empty_text.visibility = View.GONE
                    recycler_view.visibility = View.GONE
                    article_fetch_progress.visibility = View.VISIBLE
                } else {
                    article_fetch_progress.visibility = View.GONE
                }
            }
        })
    }

    private fun observeIsError() {
        articleViewModel.repository.isErrorLD.observe(this, Observer { isError ->
            isError.let {
                if (it) {
                    disableViewsOnError()
                } else {
                    empty_text.visibility = View.GONE
                    article_fetch_progress.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun observeArticleList() {
        articleViewModel.repository.articleListLD.observe(this, Observer { allArticles ->
            allArticles.let {
                if (it != null && it.isNotEmpty()) {
                    article_fetch_progress.visibility = View.VISIBLE
                    recycler_view.visibility = View.VISIBLE
                    articleAdapter.setUpArticles(it)
                    article_fetch_progress.visibility = View.GONE
                    empty_text.visibility = View.GONE
                    article_fetch_progress.visibility = View.GONE
                } else {
                    disableViewsOnError()
                }
            }
        })
    }

    private fun disableViewsOnError() {
        article_fetch_progress.visibility = View.VISIBLE
        empty_text.visibility = View.VISIBLE
        article_fetch_progress.visibility = View.GONE
        recycler_view.visibility = View.GONE
        articleAdapter.setUpArticles(emptyList())
    }
}
