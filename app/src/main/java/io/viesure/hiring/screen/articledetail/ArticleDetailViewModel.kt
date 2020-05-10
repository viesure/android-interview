package io.viesure.hiring.screen.articledetail

import android.app.Application
import android.text.Html
import android.text.SpannedString
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.viesure.hiring.util.OpenForTesting
import io.viesure.hiring.R
import io.viesure.hiring.screen.base.ErrorResolver
import io.viesure.hiring.screen.base.ErrorResolverImpl
import io.viesure.hiring.view.ErrorWidget
import io.viesure.hiring.datasource.Resource
import io.viesure.hiring.model.Article
import io.viesure.hiring.usecase.GetArticleUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@OpenForTesting
class ArticleDetailViewModel(private val getArticleUseCase: GetArticleUseCase,
                             private val application: Application) : ViewModel(),
    ErrorResolver by ErrorResolverImpl(application) {
    val articleDetailViewContent = MediatorLiveData<ArticleDetailViewContent>()
    val error = MediatorLiveData<ErrorWidget.ErrorViewContent>()
    val loadingVisibility = MediatorLiveData<Int>()

    private var previousArticle: LiveData<Resource<Article>>? = null

    companion object {
        private const val RELEASE_DATE_FORMAT = "EEE, MMM d, ''yy"
    }

    fun load(articleId: String, forceReload: Boolean = false) {
        if (!forceReload && previousArticle != null) return

        viewModelScope.launch(Dispatchers.Main) {
            //in case of a retry after an error, we stop listening for the old failed liveData
            previousArticle?.let {
                articleDetailViewContent.removeSource(it)
                error.removeSource(it)
                loadingVisibility.removeSource(it)
            }

            val article = getArticleUseCase.invoke(articleId)

            articleDetailViewContent.addSource(article) {
                when (it) {
                    is Resource.Success -> {
                        articleDetailViewContent.value = createViewContent(it.data)
                    }
                    else -> articleDetailViewContent.value = null
                }
            }

            error.addSource(article) {
                when (it) {
                    is Resource.Error -> error.value = createLoadingError(it.exception) { load(articleId, true) }
                    else -> error.value = null
                }
            }

            loadingVisibility.addSource(article) {
                when (it) {
                    Resource.Loading -> loadingVisibility.value = View.VISIBLE
                    else -> loadingVisibility.value = View.GONE
                }
            }

            previousArticle = article
        }
    }

    private fun createViewContent(article: Article) =
        ArticleDetailViewContent(
            article.title,
            article.description ?: "",
            SpannedString(Html.fromHtml(application.getString(R.string.article_details_author, article.author ?: ""))),
            article.releaseDate?.let { SimpleDateFormat(RELEASE_DATE_FORMAT, Locale.US).format(it) } ?: "",
            article.image
        )

}