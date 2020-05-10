package io.viesure.hiring.di

import androidx.lifecycle.ViewModelProvider
import io.viesure.hiring.screen.articledetail.ArticleDetailViewModel
import io.viesure.hiring.screen.articlelist.ArticleListViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val viewModelModule = Kodein.Module(DiConfig.MODULE_NAME_VIEWMODEL) {

    bind<ArticleListViewModel>() with provider {
        ArticleListViewModel(instance(), instance())
    }
    bind<ArticleDetailViewModel>() with provider {
        ArticleDetailViewModel(instance(), instance())
    }
}