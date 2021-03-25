package dev.kaitei.doggoapp.init.impl

import android.content.Context
import coil.Coil
import coil.ImageLoader
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.kaitei.doggoapp.init.Initializer
import javax.inject.Inject

class CoilInitializer @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : Initializer {

    override fun init() {
        val imageLoader = ImageLoader.Builder(applicationContext)
            .availableMemoryPercentage(percent = 0.25)
            .build()
        Coil.setImageLoader(imageLoader)
    }
}