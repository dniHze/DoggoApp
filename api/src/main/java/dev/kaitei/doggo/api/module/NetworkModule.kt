package dev.kaitei.doggo.api.module

import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Lazy
import dagger.Module
import dagger.Provides
import dev.kaitei.doggo.api.adapter.BreedAdapter
import dev.kaitei.doggo.api.retrofit.ApiResponseConverterFactory
import dev.kaitei.doggo.api.service.DoggoApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

@Module
class NetworkModule {

    @Provides
    fun moshi(): Moshi =
        Moshi.Builder()
            .addAdapter(BreedAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

    @Provides
    fun api(okHttpClient: Lazy<OkHttpClient>, moshi: Moshi): DoggoApi =
        Retrofit.Builder()
            .addConverterFactory(ApiResultConverterFactory)
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(ApiResponseConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .callFactory { request -> okHttpClient.get().newCall(request) }
            .baseUrl("https://dog.ceo/")
            .build()
            .create()
}