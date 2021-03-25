package dev.kaitei.doggo.api.impl

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.api.service.DoggoApi

open class DoggoApiMock: DoggoApi {

    override suspend fun getAllBreeds(): ApiResult<Breeds, ApiError> {
        TODO("Not yet implemented")
    }

    override suspend fun getImagesForBreed(breedId: String): ApiResult<List<String>, ApiError> {
        TODO("Not yet implemented")
    }
}