package io.viesure.exercise.data.network

import io.viesure.exercise.internal.BASE_URL
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ViesureApiService {

    fun getClient(): ViesureApi {
        val requestInterceptor = Interceptor { chain ->
            val url: HttpUrl = chain.request()
                .url
                .newBuilder()
                .build()

            val request: Request = chain.request()
                .newBuilder()
                .url(url)
                .build()

            return@Interceptor chain.proceed(request)
        }
        val okHttpClient = createOkHttpClient(requestInterceptor)

        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ViesureApi::class.java)
    }

    // Prepare to log HTTP request
    private fun createOkHttpClient(requestInterceptor: Interceptor): OkHttpClient {
        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(requestInterceptor)
            .addInterceptor(generateInterceptorCallback())
            .addInterceptor(logger)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    // Here gets the HTTP request logged to Logcat
    private fun generateInterceptorCallback(): Interceptor {
        return object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val originalRequest: Request = chain.request()
                val request = originalRequest.newBuilder().build()
                return chain.proceed(request)
            }
        }
    }
}