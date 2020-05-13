package io.viesure.hiring.screen.articledetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import io.viesure.hiring.R
import io.viesure.hiring.databinding.FragmentArticleDetailBinding
import io.viesure.hiring.screen.base.viewModelFactory
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.instance

class ArticleDetailFragment : Fragment() {
    private val kodein by kodein()
    private val viewModel by viewModels<ArticleDetailViewModel>(factoryProducer = { viewModelFactory { kodein.direct.instance<ArticleDetailViewModel>() } })

    companion object {
        const val ARG_ARTICLE_ID = "ARG_ARTICLE_ID"

        fun newInstance(articleId: String): ArticleDetailFragment {
            val instance = ArticleDetailFragment()
            instance.arguments = Bundle().also { it.putString(ARG_ARTICLE_ID, articleId) }
            return instance
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil.inflate<FragmentArticleDetailBinding>(
            inflater,
            R.layout.fragment_article_detail,
            container,
            false
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.load(arguments?.getString(ARG_ARTICLE_ID, "") ?: "")
    }
}