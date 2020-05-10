package io.viesure.hiring.screen.articlelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.viesure.hiring.R
import io.viesure.hiring.view.ArticleHeaderWidget

class ArticleAdapter : ListAdapter<ArticleHeaderWidget.ViewContent, ArticleAdapter.ArticleViewHolder>(ArticleHeaderWidget.ViewContent.DIFF_UTIL) {
    class ArticleViewHolder(private val articleHeaderWidget: ArticleHeaderWidget) : RecyclerView.ViewHolder(articleHeaderWidget) {
        fun bind(articleHeader: ArticleHeaderWidget.ViewContent) {
            articleHeaderWidget.setViewContent(articleHeader)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = ArticleHeaderWidget(parent.context)
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

@BindingAdapter("items")
fun RecyclerView.setItemsFromBinding(items: List<ArticleHeaderWidget.ViewContent>?) {
    if (this.adapter == null) {
        this.adapter = ArticleAdapter()
    }
    (this.adapter as? ArticleAdapter)?.submitList(items)
}
