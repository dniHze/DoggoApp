package dev.kaitei.feature.photo.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.feature.photo.FailureReason

const val FAILURE_CODE_UNKNOWN = -1

internal fun ApiResult.Failure<ApiError>.toFailureReason(): FailureReason =
    when (this) {
        is ApiResult.Failure.HttpFailure -> FailureReason.Api(
            error?.code ?: code,
            error?.message
        )
        is ApiResult.Failure.ApiFailure -> FailureReason.Api(
            error?.code ?: FAILURE_CODE_UNKNOWN,
            error?.message
        )
        is ApiResult.Failure.NetworkFailure -> FailureReason.Network(error)
        is ApiResult.Failure.UnknownFailure -> FailureReason.Unknown(error)
    }