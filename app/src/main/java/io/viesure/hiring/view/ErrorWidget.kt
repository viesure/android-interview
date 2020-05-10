package io.viesure.hiring.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.viesure.hiring.databinding.ViewErrorBinding

class ErrorWidget(
    context: Context,
    attrs: AttributeSet? = null
) : ConstraintLayout(context, attrs) {
    data class ErrorViewContent(
        val title: String,
        val message: String?,
        val retriable: Boolean,
        val onRetry: (() -> Unit)?
    )

    val view: View = this

    private val binding = ViewErrorBinding.inflate(LayoutInflater.from(context), this, true)

    fun setViewContent(viewContent: ErrorViewContent?) {
        visibility = if (viewContent == null) {
            View.GONE
        } else {
            View.VISIBLE
        }
        binding.viewContent = viewContent
    }
}