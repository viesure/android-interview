package io.viesure.hiring.di

import io.viesure.hiring.screen.articledetail.ArticleDetailNavigator
import io.viesure.hiring.screen.articlelist.ArticleListNavigator
import io.viesure.hiring.screen.base.Navigator
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

val navigationModule = Kodein.Module(DiConfig.MODULE_NAME_NAVIGATION) {
    bind<Navigator>() with singleton {
        Navigator(listOf(
            instance<ArticleListNavigator>(),
            instance<ArticleDetailNavigator>()
        ))
    }

    bind<ArticleListNavigator>() with singleton { ArticleListNavigator() }

    bind<ArticleDetailNavigator>() with singleton { ArticleDetailNavigator() }
}