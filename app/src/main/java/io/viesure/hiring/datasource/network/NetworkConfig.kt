package io.viesure.hiring.datasource.network

import io.viesure.hiring.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor

abstract class NetworkConfig {
    companion object {
        const val TIMEOUT_IN_SECONDS = 20L
        const val CACHE_SIZE_IN_BYTES = 10L * 1204 * 1024
    }
}