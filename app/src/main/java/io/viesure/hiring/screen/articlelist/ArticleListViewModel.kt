package io.viesure.hiring.screen.articlelist

import android.app.Application
import android.view.View
import androidx.lifecycle.*
import io.viesure.hiring.util.OpenForTesting
import io.viesure.hiring.screen.base.ErrorResolver
import io.viesure.hiring.screen.base.ErrorResolverImpl
import io.viesure.hiring.view.ErrorWidget
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.model.Article
import io.viesure.hiring.screen.articledetail.ArticleDetailNavigator
import io.viesure.hiring.screen.base.BaseViewModel
import io.viesure.hiring.screen.base.Event
import io.viesure.hiring.usecase.GetArticleListUseCase
import io.viesure.hiring.view.ArticleHeaderWidget
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OpenForTesting
class ArticleListViewModel(
    application: Application,
    private val getArticleListUseCase: GetArticleListUseCase
) : BaseViewModel(application) {
    val articleHeaders = MediatorLiveData<List<ArticleHeaderWidget.ViewContent>>()
    val error = MediatorLiveData<ErrorWidget.ErrorViewContent>()
    val loadingVisibility = MediatorLiveData<Int>()

    private var previousArticles: LiveData<Resource<List<Article>>>? = null

    fun load(forceReload: Boolean = false) {
        if (!forceReload && previousArticles != null) return
        viewModelScope.launch(Dispatchers.Main) {

            //in case of a retry after an error, we stop listening for the old, failed liveData
            previousArticles?.let {
                articleHeaders.removeSource(it)
                error.removeSource(it)
                loadingVisibility.removeSource(it)
            }

            val articles = getArticleListUseCase.invoke()

            articleHeaders.addSource(articles) {
                when (it) {
                    is Resource.Success -> {
                        articleHeaders.value =
                            it.data.map { ArticleHeaderWidget.ViewContent(it) { navigate(ArticleDetailNavigator.uri(it.id)) } }
                    }
                    else -> articleHeaders.value = null
                }
            }

            error.addSource(articles) {
                when (it) {
                    is Resource.Error -> error.value = createLoadingError(it.exception, { load(true) })
                    else -> error.value = null
                }
            }

            loadingVisibility.addSource(articles) {
                when (it) {
                    Resource.Loading -> loadingVisibility.value = View.VISIBLE
                    else -> loadingVisibility.value = View.GONE
                }
            }

            previousArticles = articles
        }
    }
}