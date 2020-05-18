package io.viesure.exercise.view.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.viesure.exercise.R
import io.viesure.exercise.databinding.ItemArticleBinding
import io.viesure.exercise.internal.*
import io.viesure.exercise.model.Article
import io.viesure.exercise.view.ui.ArticleDetailsActivity


class ArticleAdapter(
    private var articleList: ArrayList<Article>
) : RecyclerView.Adapter<ArticleViewHolder>() {

    private lateinit var viewGroupContext: Context


    override fun getItemCount(): Int = articleList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        viewGroupContext = parent.context
        val itemArticleBinding: ItemArticleBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_article,
            parent,
            false
        )
        return ArticleViewHolder(itemArticleBinding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.itemArticleBinding.article = articleList[position]
        holder.itemArticleBinding.articleCard.setOnClickListener {
            setIntentForArticleDetails(articleList[position])
        }
    }

    fun setUpArticles(articles: List<Article>) {
        articleList.clear()
        articleList.addAll(articles)
        notifyDataSetChanged()
    }

    private fun setIntentForArticleDetails(article: Article) {
        val newsDetailIntent = Intent(viewGroupContext, ArticleDetailsActivity::class.java)
        newsDetailIntent.putExtra(ARTICLE_IMAGE, article.image)
        newsDetailIntent.putExtra(ARTICLE_TITLE, article.title)
        newsDetailIntent.putExtra(ARTICLE_RELEASE_DATE, article.releaseDate)
        newsDetailIntent.putExtra(ARTICLE_DESCRIPTION, article.description)
        newsDetailIntent.putExtra(ARTICLE_AUTHOR, article.author)
        viewGroupContext.startActivity(newsDetailIntent)
    }
}

class ArticleViewHolder(val itemArticleBinding: ItemArticleBinding) :
    RecyclerView.ViewHolder(itemArticleBinding.root)