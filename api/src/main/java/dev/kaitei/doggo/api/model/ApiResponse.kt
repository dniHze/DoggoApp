package dev.kaitei.doggo.api.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T : Any>(
    @Json(name = "message") val message: T,
    @Json(name = "status") val status: String
)