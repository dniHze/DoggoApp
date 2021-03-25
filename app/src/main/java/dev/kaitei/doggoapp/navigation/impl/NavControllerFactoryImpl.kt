package dev.kaitei.doggoapp.navigation.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dev.kaitei.doggo.navigation.NavEntryFactory
import dev.kaitei.doggoapp.navigation.NavControllerOwner
import dev.kaitei.doggoapp.navigation.NavGraphFactory
import dev.kaitei.feature.list.navigation.BreedListEntryFactory
import javax.inject.Inject

@ActivityRetainedScoped
class NavControllerFactoryImpl @Inject constructor(
    private val entryFactories: Set<@JvmSuppressWildcards NavEntryFactory>
) : NavGraphFactory, NavControllerOwner {

    override var navController: NavController? = null
        private set

    @Composable
    override fun Create() {
        val controller = rememberNavController()
        navController = controller
        DisposableEffect("navigator") {
            onDispose {
                navController = null
            }
        }

        NavHost(navController = controller, startDestination = BreedListEntryFactory.route) {
            for (entry in entryFactories) {
                entry.buildEntry(this@NavHost)
            }
        }
    }
}