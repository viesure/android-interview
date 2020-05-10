package io.viesure.hiring.di

import io.viesure.hiring.datasource.repository.ArticleRepository
import io.viesure.hiring.datasource.repository.ArticleRepositoryImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val repositoryModule = Kodein.Module(DiConfig.MODULE_NAME_REPOSITORY) {
    bind<ArticleRepository>() with provider {
        ArticleRepositoryImpl(instance(), instance(), instance(DiConfig.TAG_IO_DISPATCHER))
    }
}