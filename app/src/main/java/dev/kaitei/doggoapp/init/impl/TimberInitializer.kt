package dev.kaitei.doggoapp.init.impl

import dev.kaitei.doggoapp.BuildConfig
import dev.kaitei.doggoapp.init.Initializer
import timber.log.Timber
import javax.inject.Inject

class TimberInitializer @Inject constructor() : Initializer {

    override fun init() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}