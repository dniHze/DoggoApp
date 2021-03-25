package dev.kaitei.doggoapp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.kaitei.doggo.api.module.ApiModule
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module(includes = [ApiModule::class])
class NetworkModule {

    @Provides
    fun okHttp(): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .callTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor { message ->
                    Timber.tag("OkHttpClient").i(message)
                }.apply { setLevel(HttpLoggingInterceptor.Level.BASIC) }
            )
            .build()

    companion object {
        const val DEFAULT_TIMEOUT = 30L
    }
}