package dev.kaitei.doggoapp.navigation.photo

import dev.kaitei.doggoapp.navigation.NavControllerOwner
import dev.kaitei.feature.photo.navigation.PhotoDirections
import javax.inject.Inject

class BreedPhotoDirections @Inject constructor(
    private val navControllerOwner: NavControllerOwner
) : PhotoDirections {
    override fun goBack() {
        navControllerOwner.navController?.popBackStack()
    }
}