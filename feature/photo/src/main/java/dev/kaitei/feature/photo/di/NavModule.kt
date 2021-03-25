package dev.kaitei.feature.photo.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.kaitei.doggo.navigation.NavEntryFactory
import dev.kaitei.feature.photo.navigation.PhotoEntryFactory

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class NavModule {

    @Binds
    @IntoSet
    abstract fun PhotoEntryFactory.bindsEntryFactory(): NavEntryFactory
}