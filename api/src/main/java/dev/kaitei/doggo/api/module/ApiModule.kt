package dev.kaitei.doggo.api.module

import dagger.Binds
import dagger.Module
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.doggo.api.impl.RetrofitDoggoRepository
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
abstract class ApiModule {

    @Binds
    @Singleton
    abstract fun RetrofitDoggoRepository.bindDoggoRepo(): DoggoRepository
}