package dev.kaitei.feature.list.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.model.Breed
import dev.kaitei.feature.list.Msg
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class UseCasesTest {

    @Test
    fun `on call requests repo`() {
        val repo = object : MockRepo() {
            var called = false
            override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                called = true
                return ApiResult.unknownFailure(Exception())
            }
        }
        val case = loadBreads(repo)
        runBlockingTest {
            case.invoke(this) {

            }
            assertTrue(repo.called)
        }
    }

    @Test
    fun `on success call dispatches success message`() {
        val repo = object : MockRepo() {
            override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                return ApiResult.Success(emptyList())
            }
        }
        val case = loadBreads(repo)
        runBlockingTest {
            case.invoke(this) {
                assertTrue(it is Msg.ApiSuccess)
            }
        }
    }

    @Test
    fun `on success call dispatches success message with same data`() {
        val repo = object : MockRepo() {
            override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                return ApiResult.Success(emptyList())
            }
        }
        val case = loadBreads(repo)
        runBlockingTest {
            case.invoke(this) {
                val message = it as Msg.ApiSuccess
                assertEquals(emptyList<Breed>(), message.data)
            }
        }
    }

    @Test
    fun `on failed call dispatches failure message`() {
        val repo = object : MockRepo() {
            override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                return ApiResult.unknownFailure(Throwable())
            }
        }
        val case = loadBreads(repo)
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
            override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                return failure
            }
        }
        val case = loadBreads(repo)
        runBlockingTest {
            case.invoke(this) {
                val message = it as Msg.ApiFailure
                assertEquals(failure, message.failure)
            }
        }
    }
}