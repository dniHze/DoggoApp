package dev.kaitei.feature.list.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.feature.list.Msg
import oolong.effect
import timber.log.Timber
import java.lang.IllegalStateException

internal fun loadBreads(repo: DoggoRepository) = effect<Msg> { dispatch ->
    val msg = when (val requestResult = repo.getAllBreeds()) {
        is ApiResult.Success -> Msg.ApiSuccess(requestResult.response)
        is ApiResult.Failure -> {
            Timber.tag("loadBreeds").e("Error on calling API: %s", requestResult)
            Msg.ApiFailure(requestResult)
        }
        else -> throw IllegalStateException()
    }
    dispatch(msg)
}