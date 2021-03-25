package dev.kaitei.doggoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import dev.kaitei.doggoapp.init.Initializer
import dev.kaitei.doggoapp.init.impl.CoilInitializer
import dev.kaitei.doggoapp.init.impl.TimberInitializer

@Module
@InstallIn(SingletonComponent::class)
abstract class InitializerModule {

    @Binds
    @IntoSet
    abstract fun TimberInitializer.bindTimberInitializer(): Initializer

    @Binds
    @IntoSet
    abstract fun CoilInitializer.bindCoilInitializer(): Initializer
}