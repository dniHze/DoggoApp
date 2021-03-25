package dev.kaitei.doggoapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dev.kaitei.doggoapp.navigation.list.BreedListDirections
import dev.kaitei.doggoapp.navigation.photo.BreedPhotoDirections
import dev.kaitei.feature.list.navigation.ListDirections
import dev.kaitei.feature.photo.navigation.PhotoDirections

@InstallIn(ActivityRetainedComponent::class)
@Module
abstract class DirectionsModule {

    @Binds
    abstract fun BreedListDirections.bindListDirections(): ListDirections

    @Binds
    abstract fun BreedPhotoDirections.bindPhotoDirections(): PhotoDirections
}