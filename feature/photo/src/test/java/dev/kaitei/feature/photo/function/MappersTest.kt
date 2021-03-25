package dev.kaitei.feature.photo.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.feature.photo.FailureReason
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException

internal class MappersTest {

    @Nested
    @DisplayName("toFailureReason")
    inner class ToFailureReason {

        @Test
        fun `on network error create network reason`() {
            val failure: ApiResult.Failure<ApiError> = ApiResult.networkFailure(IOException())
            val reason = failure.toFailureReason()
            assertTrue(reason is FailureReason.Network)
        }

        @Test
        fun `on network error create network reason with same exception`() {
            val e = IOException()
            val failure: ApiResult.Failure<ApiError> = ApiResult.networkFailure(e)
            val reason = failure.toFailureReason()
            val networkReason = reason as FailureReason.Network
            assertEquals(e, networkReason.exception)
        }

        @Test
        fun `on api error creates network reason`() {
            val failure: ApiResult.Failure<ApiError> = ApiResult.apiFailure()
            val reason = failure.toFailureReason()
            assertTrue(reason is FailureReason.Api)
        }

        @Test
        fun `on api error with null model creates api reason with default params`() {
            val failure: ApiResult.Failure<ApiError> = ApiResult.apiFailure()
            val reason = failure.toFailureReason()
            val apiReason = reason as FailureReason.Api
            assertNull(apiReason.message)
            assertEquals(FAILURE_CODE_UNKNOWN, apiReason.code)
        }

        @Test
        fun `on api error with no null model creates api reason with same params`() {
            val apiError = ApiError(400, "hello world", "error")
            val failure: ApiResult.Failure<ApiError> = ApiResult.apiFailure(apiError)
            val reason = failure.toFailureReason()
            val apiReason = reason as FailureReason.Api
            assertEquals(apiError.message, apiReason.message)
            assertEquals(apiError.code, apiReason.code)
        }

        @Test
        fun `on http error creates api reason`() {
            val failure: ApiResult.Failure<ApiError> = ApiResult.httpFailure(400)
            val reason = failure.toFailureReason()
            assertTrue(reason is FailureReason.Api)
        }

        @Test
        fun `on http error creates api reason with same code`() {
            val failure = ApiResult.httpFailure<ApiError>(400)
            val reason = failure.toFailureReason() as FailureReason.Api
            assertEquals(failure.code, reason.code)
        }

        @Test
        fun `on http error creates api reason with null message`() {
            val failure = ApiResult.httpFailure<ApiError>(400)
            val reason = failure.toFailureReason() as FailureReason.Api
            assertNull(reason.message)
        }

        @Test
        fun `on http error with ApiError creates api reason with same params to ApiError`() {
            val apiError = ApiError(400, "hello world", "error")
            val failure = ApiResult.httpFailure(500, apiError)
            val reason = failure.toFailureReason() as FailureReason.Api
            assertEquals(apiError.message, reason.message)
            assertEquals(apiError.code, reason.code)
        }

        @Test
        fun `on unknown error create unknown reason`() {
            val failure: ApiResult.Failure<ApiError> = ApiResult.unknownFailure(IOException())
            val reason = failure.toFailureReason()
            assertTrue(reason is FailureReason.Unknown)
        }

        @Test
        fun `on unknown error create unknown reason with same exception`() {
            val e = IOException()
            val failure: ApiResult.Failure<ApiError> = ApiResult.unknownFailure(e)
            val reason = failure.toFailureReason() as FailureReason.Unknown
            assertEquals(e, reason.throwable)
        }
    }
}