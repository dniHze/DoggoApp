package dev.kaitei.doggoapp.navigation.list

import dev.kaitei.doggo.model.BaseBreed
import dev.kaitei.doggoapp.navigation.NavControllerOwner
import dev.kaitei.feature.list.navigation.ListDirections
import dev.kaitei.feature.photo.navigation.PhotoEntryFactory
import javax.inject.Inject

class BreedListDirections @Inject constructor(
    private val navControllerOwner: NavControllerOwner
) : ListDirections {
    override fun openPhotos(baseBreed: BaseBreed) {
        with(PhotoEntryFactory) {
            navControllerOwner.navController?.navigateToPhoto(baseBreed)
        }
    }
}