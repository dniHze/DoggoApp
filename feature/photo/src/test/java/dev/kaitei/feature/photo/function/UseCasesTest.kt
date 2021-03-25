package dev.kaitei.feature.photo.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.feature.photo.Msg
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UseCasesTest {

    @Test
    fun `on call requests repo`() {
        val repo = object : MockRepo() {
            var called = false
            override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                called = true
                return ApiResult.unknownFailure(Exception())
            }
        }
        val case = loadPhotos("foo", repo)
        runBlockingTest {
            case.invoke(this) {

            }
            assertTrue(repo.called)
        }
    }

    @Test
    fun `on call requests repo with same param`() {
        val repo = object : MockRepo() {
            var id: String? = null
            override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                this.id = id
                return ApiResult.unknownFailure(Exception())
            }
        }
        val case = loadPhotos("foo", repo)
        runBlockingTest {
            case.invoke(this) {

            }
            assertEquals("foo", repo.id)
        }
    }

    @Test
    fun `on success call dispatches success message`() {
        val repo = object : MockRepo() {
            override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                return ApiResult.Success(emptyList())
            }
        }
        val case = loadPhotos("foo", repo)
        runBlockingTest {
            case.invoke(this) {
                assertTrue(it is Msg.ApiSuccess)
            }
        }
    }

    @Test
    fun `on success call dispatches success message with same data`() {
        val repo = object : MockRepo() {
            override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                return ApiResult.Success(emptyList())
            }
        }
        val case = loadPhotos("foo", repo)
        runBlockingTest {
            case.invoke(this) {
                val message = it as Msg.ApiSuccess
                assertEquals(emptyList<String>(), message.data)
            }
        }
    }

    @Test
    fun `on failed call dispatches failure message`() {
        val repo = object : MockRepo() {
            override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                return ApiResult.unknownFailure(Throwable())
            }
        }
        val case = loadPhotos("foo", repo)
        runBlockingTest {
            case.invoke(this) {
                assertTrue(it is Msg.ApiFailure)
            }
        }
    }

    @Test
    fun `on failed call dispatches failure message with same failure`() {
        val failure = ApiResult.unknownFailure(Throwable())
        val repo = object : MockRepo() {
            override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                return failure
            }
        }
        val case = loadPhotos("foo", repo)
        runBlockingTest {
            case.invoke(this) {
                val message = it as Msg.ApiFailure
                assertEquals(failure, message.failure)
            }
        }
    }
}