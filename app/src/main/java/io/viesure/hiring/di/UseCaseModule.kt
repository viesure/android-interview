package io.viesure.hiring.di

import io.viesure.hiring.usecase.GetArticleListUseCase
import io.viesure.hiring.usecase.GetArticleListUseCaseImpl
import io.viesure.hiring.usecase.GetArticleUseCase
import io.viesure.hiring.usecase.GetArticleUseCaseImpl
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val useCaseModule = Kodein.Module(DiConfig.MODULE_NAME_USECASE) {
    bind<GetArticleListUseCase>() with provider { GetArticleListUseCaseImpl(instance(), instance(DiConfig.TAG_DEFAULT_DISPATCHER)) }
    bind<GetArticleUseCase>() with provider { GetArticleUseCaseImpl(instance()) }
}