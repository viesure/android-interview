package io.viesure.hiring.view

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import io.viesure.hiring.databinding.ViewArticleHeaderBinding
import io.viesure.hiring.model.Article
import java.util.*

class ArticleHeaderWidget(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {
    data class ViewContent(
        val id: String,
        val title: String,
        val description: String?,
        val image: Uri?,
        val onClickAction: (() -> Unit)?
    ) {

        constructor(article: Article, onClickAction: (() -> Unit)?) : this(article.id, article.title, article.description, article.image, onClickAction)

        companion object {
            val DIFF_UTIL = object : DiffUtil.ItemCallback<ViewContent>() {
                override fun areItemsTheSame(oldItem: ViewContent, newItem: ViewContent) = oldItem.id == newItem.id

                override fun areContentsTheSame(oldItem: ViewContent, newItem: ViewContent): Boolean {
                    return oldItem.title == newItem.title &&
                            oldItem.description == newItem.description &&
                            oldItem.image == newItem.image
                }

            }
        }

        override fun equals(other: Any?): Boolean {
            if(other !is ViewContent) return false
            return id == other.id &&
                    title == other.title &&
                    description == other.description &&
                    image == other.image
        }

        override fun hashCode(): Int {
            return Objects.hash(id, title, description, image)
        }
    }

    private val binding = ViewArticleHeaderBinding.inflate(LayoutInflater.from(context), this, true)

    fun setViewContent(viewContent: ViewContent?) {
        binding.viewContent = viewContent
    }

}