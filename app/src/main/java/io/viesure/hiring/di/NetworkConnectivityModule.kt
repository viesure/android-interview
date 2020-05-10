package io.viesure.hiring.di

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.viesure.hiring.BuildConfig
import io.viesure.hiring.datasource.network.NetworkConfig
import io.viesure.hiring.datasource.network.typeadapters.DateTypeAdapter
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

val networkConnectivityModule = Kodein.Module(DiConfig.MODULE_NAME_NETWORK) {

    bind<Cache>() with provider {
        Cache(instance<Context>().cacheDir, NetworkConfig.CACHE_SIZE_IN_BYTES)
    }

    bind<HttpLoggingInterceptor.Level>() with singleton { HttpLoggingInterceptor.Level.BODY }

    bind<OkHttpClient>() with provider {
        val builder = OkHttpClient.Builder()
        val logLevel: HttpLoggingInterceptor.Level = instance()
        if (BuildConfig.DEBUG && (logLevel != HttpLoggingInterceptor.Level.NONE)) {
            builder.addInterceptor(HttpLoggingInterceptor().also {
                it.level = logLevel
            })
        }

        builder.connectTimeout(NetworkConfig.TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    bind<Moshi>() with provider {
        Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .add(DateTypeAdapter())
            .build()
    }

    bind<MoshiConverterFactory>() with provider {
        MoshiConverterFactory.create(instance())
    }

    bind<String>(tag = DiConfig.TAG_BASE_URL) with singleton { BuildConfig.BASE_URL }

    bind<Retrofit>() with singleton {
        Retrofit.Builder()
            .client(instance())
            .baseUrl(instance<String>(DiConfig.TAG_BASE_URL))
            .addConverterFactory(instance<MoshiConverterFactory>())
            .build()
    }
}