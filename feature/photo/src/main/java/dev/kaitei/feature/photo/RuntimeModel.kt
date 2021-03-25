package dev.kaitei.feature.photo

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import oolong.Dispatch
import java.io.IOException

internal data class Model(
    val id: String,
    val fullName: String,
    val isLoading: Boolean,
    val failure: FailureReason? = null,
    val data: List<String>? = null,
)

internal sealed class Msg {
    object LoadData : Msg()
    data class ApiSuccess(val data: List<String>) : Msg()
    data class ApiFailure(val failure: ApiResult.Failure<ApiError>) : Msg()
}

internal data class Props(
    val fullName: String,
    val data: DataProps,
    val onBackPressed: () -> Unit
)

internal sealed class DataProps {
    object Loading : DataProps()

    data class Success(
        val photos: List<String>,
    ) : DataProps()

    data class Failure(
        val reason: FailureReason,
        val onRetryClicked: (Dispatch<Msg>) -> Unit
    ) : DataProps()
}

sealed class FailureReason {
    data class Api(val code: Int, val message: String?) : FailureReason()
    data class Network(val exception: IOException) : FailureReason()
    data class Unknown(val throwable: Throwable) : FailureReason()
}
