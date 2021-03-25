package dev.kaitei.feature.photo.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.feature.photo.Msg
import oolong.effect
import timber.log.Timber
import java.lang.IllegalStateException

internal fun loadPhotos(id: String, repo: DoggoRepository) = effect<Msg> { dispatch ->
    val msg = when (val requestResult = repo.getBreedPhotos(id)) {
        is ApiResult.Success -> Msg.ApiSuccess(requestResult.response)
        is ApiResult.Failure -> {
            Timber.tag("loadPhotos").e("Error on calling API: %s", requestResult)
            Msg.ApiFailure(requestResult)
        }
        else -> throw IllegalStateException()
    }
    dispatch(msg)
}