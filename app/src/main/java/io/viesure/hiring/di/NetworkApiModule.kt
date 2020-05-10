package io.viesure.hiring.di

import io.viesure.hiring.datasource.network.api.ArticleApi
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

val networkApiModule = Kodein.Module(DiConfig.MODULE_NAME_NETWORK_API) {
    bind<ArticleApi>() with provider {
        kodein.direct.instance<Retrofit>().create(ArticleApi::class.java)
    }
}