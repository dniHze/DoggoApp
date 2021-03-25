package dev.kaitei.feature.list.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.model.Breed
import dev.kaitei.doggo.model.SubBreed
import dev.kaitei.feature.list.FailureReason
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.io.IOException

internal class MappersTest {

    @Nested
    @DisplayName("asSortedBreedSegments")
    inner class SortedBreedSegment {

        @Test
        fun `on empty creates empty`() {
            val data: List<Breed> = emptyList()
            val mapped = data.asSortedBreedSegments()
            assertEquals(emptyList<SortedBreedSegment>(), mapped)
        }

        @Test
        fun `on one item creates one item with same capital`() {
            val data: List<Breed> = listOf(
                Breed("foo", "foo", emptyList())
            )
            val mapped = data.asSortedBreedSegments()
            assertEquals("F", mapped.first().segmentCapitalLetter)
        }

        @Test
        fun `on one item creates one item with same breed`() {
            val data: List<Breed> = listOf(
                Breed("foo", "foo", emptyList())
            )
            val mapped = data.asSortedBreedSegments()
            assertEquals(data, mapped.first().breeds)
        }

        @Test
        fun `does not sorts subbreeds of breed`() {
            val subs = listOf(
                SubBreed("foo/zetta", "zetta", "foo"),
                SubBreed("foo/bar", "bar", "foo")
            )
            val data: List<Breed> = listOf(
                Breed("foo", "foo", subs)
            )
            val mapped = data.asSortedBreedSegments()
            assertEquals(subs, mapped.first().breeds.first().subs)
        }

        @Test
        fun `on grouping sorts breeds`() {
            val foo = Breed("foo", "foo", emptyList())
            val faz = Breed("faz", "faz", emptyList())
            val data: List<Breed> = listOf(foo, faz)
            val mapped = data.asSortedBreedSegments()
            assertEquals(listOf(faz, foo), mapped.first().breeds)
        }

        @Test
        fun `on grouping sorts breeds even if sorted`() {
            val foo = Breed("foo", "foo", emptyList())
            val faz = Breed("faz", "faz", emptyList())
            val data: List<Breed> = listOf(faz, foo)
            val mapped = data.asSortedBreedSegments()
            assertEquals(listOf(faz, foo), mapped.first().breeds)
        }

        @Test
        fun `creates new group of breeds for each capital`() {
            val foo = Breed("foo", "foo", emptyList())
            val faz = Breed("alpha", "alpha", emptyList())
            val data: List<Breed> = listOf(faz, foo)
            val mapped = data.asSortedBreedSegments()
            assertEquals(2, mapped.size)
            assertEquals("A", mapped[0].segmentCapitalLetter)
            assertEquals("F", mapped[1].segmentCapitalLetter)
        }
    }

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
            val failure = ApiResult.httpFailure<ApiError>(500, apiError)
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