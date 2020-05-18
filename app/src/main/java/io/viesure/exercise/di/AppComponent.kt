package io.viesure.exercise.di

import dagger.Component
import io.viesure.exercise.repository.ArticleRepositoryImpl
import io.viesure.exercise.view.ui.MainActivity
import io.viesure.exercise.viewmodel.ArticleViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    fun inject(articleViewModel: ArticleViewModel)

    fun inject(articleRepositoryImpl: ArticleRepositoryImpl)
}