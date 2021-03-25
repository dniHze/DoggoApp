package dev.kaitei.feature.list

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.model.BaseBreed
import dev.kaitei.doggo.model.Breed
import dev.kaitei.feature.list.model.BreedListSegment
import oolong.Dispatch
import java.io.IOException

internal data class Model(
    val isLoading: Boolean,
    val failure: FailureReason? = null,
    val data: List<BreedListSegment>? = null,
)

internal sealed class Msg {
    object LoadData : Msg()
    data class ApiSuccess(val data: List<Breed>) : Msg()
    data class ApiFailure(val failure: ApiResult.Failure<ApiError>) : Msg()
}

internal sealed class Props {
    object Loading : Props()

    data class Success(
        val breeds: List<BreedListSegment>,
        val onBreedClicked: (BaseBreed) -> Unit
    ) : Props()

    data class Failure(
        val reason: FailureReason,
        val onRetryClicked: (Dispatch<Msg>) -> Unit
    ) : Props()
}

sealed class FailureReason {
    data class Api(val code: Int, val message: String?) : FailureReason()
    data class Network(val exception: IOException) : FailureReason()
    data class Unknown(val throwable: Throwable) : FailureReason()
}
