package dev.kaitei.doggoapp.init

import javax.inject.Inject

class AppInitializer @Inject constructor(
    private val initializers: Set<@JvmSuppressWildcards Initializer>
) : Initializer {
    override fun init() {
        initializers.forEach(Initializer::init)
    }
}