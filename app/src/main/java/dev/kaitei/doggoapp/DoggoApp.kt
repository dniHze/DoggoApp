package dev.kaitei.doggoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.kaitei.doggoapp.init.AppInitializer
import javax.inject.Inject

@HiltAndroidApp
class DoggoApp : Application() {

    @Inject
    lateinit var appInitializer: AppInitializer

    override fun onCreate() {
        super.onCreate()
        appInitializer.init()
    }
}