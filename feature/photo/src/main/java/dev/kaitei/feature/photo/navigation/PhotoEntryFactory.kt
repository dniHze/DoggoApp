package dev.kaitei.feature.photo.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.compose.navigate
import dev.kaitei.doggo.model.BaseBreed
import dev.kaitei.doggo.navigation.NavEntryFactory
import dev.kaitei.feature.photo.PhotoViewModel
import dev.kaitei.feature.photo.ui.BreedPhotos
import javax.inject.Inject

class PhotoEntryFactory @Inject constructor() : NavEntryFactory {

    override fun buildEntry(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable(
            route = route,
            arguments = listOf(
                navArgument(argumentId) {
                    type = NavType.StringType
                    nullable = false
                },
                navArgument(argumentName) {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val id = entry.arguments?.getString(argumentId)!!
            val name = entry.arguments?.getString(argumentName)!!
            val viewModel = hiltNavGraphViewModel<PhotoViewModel>()
            BreedPhotos(id = id, fullName = name, viewModel = viewModel)
        }
    }

    companion object {
        const val route = "photo/{breedId}?breedName={breedName}"

        private const val argumentId = "breedId"
        private const val argumentName = "breedName"

        fun NavController.navigateToPhoto(breed: BaseBreed) {
            navigate("photo/${breed.id}?breedName=${breed.fullDisplayName}")
        }
    }
}