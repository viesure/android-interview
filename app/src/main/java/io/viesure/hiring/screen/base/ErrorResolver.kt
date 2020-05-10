package io.viesure.hiring.screen.base

import android.app.Application
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import io.viesure.hiring.R
import io.viesure.hiring.datasource.database.DatabaseException
import io.viesure.hiring.exception.ArticleNotFoundException
import io.viesure.hiring.view.ErrorWidget
import retrofit2.HttpException
import java.io.IOException

interface ErrorResolver {
    fun createLoadingError(throwable: Throwable, onRetry: (() -> Unit)?): ErrorWidget.ErrorViewContent
    fun getErrorTitle(throwable: Throwable): String
    fun getErrorDescription(throwable: Throwable): String?
}

class ErrorResolverImpl(private val context: Application) : ErrorResolver {

    /**
     * Returns an error to show on an ErrorWidget, which has to be added to the screens layout
     */
    override fun createLoadingError(throwable: Throwable, onRetry: (() -> Unit)?): ErrorWidget.ErrorViewContent {
        return ErrorWidget.ErrorViewContent(
            getErrorTitle(throwable),
            getErrorDescription(throwable),
            true,
            onRetry
        )
    }

    override fun getErrorTitle(throwable: Throwable): String {
        return when (throwable) {
            is JsonEncodingException -> context.getString(R.string.json_parse_error_title)
            is JsonDataException -> context.getString(R.string.json_parse_error_title)
            is HttpException -> context.getString(R.string.backend_error_title)
            is IOException -> context.getString(R.string.no_internet_title)
            is DatabaseException -> context.getString(R.string.database_error_title)
            is ArticleNotFoundException -> context.getString(R.string.article_not_found_error_title)
            else -> context.getString(R.string.common_unknown_error_title)
        }
    }

    override fun getErrorDescription(throwable: Throwable): String? {
        return when (throwable) {
            is JsonEncodingException -> context.getString(R.string.json_parse_error_description)
            is JsonDataException -> context.getString(R.string.json_parse_error_description)
            is HttpException -> context.getString(R.string.backend_error_description, throwable.code().toString())
            is IOException -> context.getString(R.string.no_internet_description)
            is DatabaseException -> context.getString(R.string.database_error_description)
            is ArticleNotFoundException -> context.getString(R.string.article_not_found_error_description)
            else -> context.getString(R.string.common_unknown_error_description)
        }
    }
}