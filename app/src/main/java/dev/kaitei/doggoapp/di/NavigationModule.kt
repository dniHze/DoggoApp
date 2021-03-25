package dev.kaitei.doggoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dev.kaitei.doggoapp.navigation.NavControllerOwner
import dev.kaitei.doggoapp.navigation.NavGraphFactory
import dev.kaitei.doggoapp.navigation.impl.NavControllerFactoryImpl

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class NavigationModule {

    @Binds
    abstract fun NavControllerFactoryImpl.bindNavGraphFactory(): NavGraphFactory

    @Binds
    abstract fun NavControllerFactoryImpl.bindNavControllerOwner(): NavControllerOwner
}