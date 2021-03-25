package dev.kaitei.doggo.api.service

import com.slack.eithernet.ApiResult
import com.slack.eithernet.DecodeErrorBody
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.api.model.ApiError
import retrofit2.http.GET
import retrofit2.http.Path

interface DoggoApi {
    @DecodeErrorBody
    @GET("api/breeds/list/all")
    suspend fun getAllBreeds(): ApiResult<Breeds, ApiError>

    @DecodeErrorBody
    @GET("api/breed/{breedId}/images")
    suspend fun getImagesForBreed(
        @Path("breedId", encoded = true) breedId: String
    ): ApiResult<List<String>, ApiError>
}