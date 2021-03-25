package dev.kaitei.doggo.api.impl

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.api.service.DoggoApi
import javax.inject.Inject

class RetrofitDoggoRepository @Inject constructor(
    private val doggoApi: DoggoApi
) : DoggoRepository {

    override suspend fun getAllBreeds(): ApiResult<Breeds, ApiError> = doggoApi.getAllBreeds()

    override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> =
        doggoApi.getImagesForBreed(id)
}