package dev.kaitei.doggo.api.impl

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.api.model.ApiError
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class RetrofitDoggoRepositoryTest {

    @Test
    fun `getAllBreeds returns what DoggoApi returns on success`() = runBlockingTest {
        val api = object : DoggoApiMock() {
            override suspend fun getAllBreeds(): ApiResult<Breeds, ApiError> {
                return ApiResult.Success(emptyList())
            }
        }

        val repo = RetrofitDoggoRepository(api)
        assertEquals(ApiResult.Success<Breeds>(emptyList()), repo.getAllBreeds())
    }

    @Test
    fun `getAllBreeds returns what DoggoApi returns on error`() = runBlockingTest {
        val api = object : DoggoApiMock() {
            override suspend fun getAllBreeds(): ApiResult<Breeds, ApiError> {
                return ApiResult.unknownFailure(Throwable())
            }
        }

        val repo = RetrofitDoggoRepository(api)
        assertTrue(repo.getAllBreeds() is ApiResult.Failure.UnknownFailure)
    }

    @Test
    fun `getAllBreeds call DoggoApi exactly once`() = runBlockingTest {
        val api = object : DoggoApiMock() {
            var callCount = 0
            override suspend fun getAllBreeds(): ApiResult<Breeds, ApiError> {
                callCount++
                return ApiResult.Success(emptyList())
            }
        }

        val repo = RetrofitDoggoRepository(api)
        repo.getAllBreeds()
        assertEquals(1, api.callCount)
    }


    @Test
    fun `getImagesForBreed returns what DoggoApi returns on success`() = runBlockingTest {
        val api = object : DoggoApiMock() {
            override suspend fun getImagesForBreed(breedId: String): ApiResult<List<String>, ApiError> {
                return ApiResult.Success(listOf("image"))
            }
        }

        val repo = RetrofitDoggoRepository(api)
        assertEquals(ApiResult.Success(listOf("image")), repo.getBreedPhotos("foo"))
    }

    @Test
    fun `getImagesForBreed returns what DoggoApi returns on error`() = runBlockingTest {
        val api = object : DoggoApiMock() {
            override suspend fun getImagesForBreed(breedId: String): ApiResult<List<String>, ApiError> {
                return ApiResult.unknownFailure(Throwable())
            }
        }
        val repo = RetrofitDoggoRepository(api)
        assertTrue(repo.getBreedPhotos("foo") is ApiResult.Failure.UnknownFailure)
    }

    @Test
    fun `getImagesForBreed call DoggoApi exactly once`() = runBlockingTest {
        val api = object : DoggoApiMock() {
            var callCount = 0

            override suspend fun getImagesForBreed(breedId: String): ApiResult<List<String>, ApiError> {
                callCount++
                return ApiResult.Success(listOf("image"))
            }

        }
        val repo = RetrofitDoggoRepository(api)
        repo.getBreedPhotos("foo")
        assertEquals(1, api.callCount)
    }

    @Test
    fun `getImagesForBreed call DoggoApi with same param `() = runBlockingTest {
        val api = object : DoggoApiMock() {
            var param = ""

            override suspend fun getImagesForBreed(breedId: String): ApiResult<List<String>, ApiError> {
                param = breedId
                return ApiResult.Success(listOf("image"))
            }

        }
        val repo = RetrofitDoggoRepository(api)
        repo.getBreedPhotos("foo")
        assertEquals("foo", api.param)
    }
}