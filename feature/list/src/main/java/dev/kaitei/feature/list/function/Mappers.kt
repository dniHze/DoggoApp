package dev.kaitei.feature.list.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.model.Breed
import dev.kaitei.feature.list.FailureReason
import dev.kaitei.feature.list.model.BreedListSegment
import java.util.*

const val FAILURE_CODE_UNKNOWN = -1

internal fun List<Breed>.asSortedBreedSegments(): List<BreedListSegment> =
    groupBy { breed -> breed.name.take(1).toUpperCase(Locale.ENGLISH) }
        .map { (capital, breeds) ->
            BreedListSegment(capital, breeds.sorted())
        }
        .sorted()

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
