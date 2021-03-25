package dev.kaitei.doggo.api

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError

interface DoggoRepository {
    suspend fun getAllBreeds(): ApiResult<Breeds, ApiError>

    suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError>
}