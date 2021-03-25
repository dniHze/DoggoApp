package dev.kaitei.feature.list.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import dev.kaitei.doggo.navigation.NavEntryFactory
import dev.kaitei.feature.list.navigation.BreedListEntryFactory

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class NavModule {

    @Binds
    @IntoSet
    abstract fun BreedListEntryFactory.bindsEntryFactory(): NavEntryFactory
}