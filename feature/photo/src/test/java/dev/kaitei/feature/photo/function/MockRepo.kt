package dev.kaitei.feature.photo.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.model.Breed

open class MockRepo: DoggoRepository {
    override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
        TODO("Not yet implemented")
    }

    override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
        TODO("Not yet implemented")
    }
}