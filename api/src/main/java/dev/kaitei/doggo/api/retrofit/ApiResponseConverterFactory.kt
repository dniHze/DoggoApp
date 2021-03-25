package dev.kaitei.doggo.api.retrofit

import com.slack.eithernet.ApiResult
import com.slack.eithernet.Types
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.api.model.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * A custom [Converter.Factory]. Basically it just takes the parametrized
 * type for the request and delegates parsing it to Moshi ConverterFactory by
 * wrapping it to [ApiResponse] implicitly, parsing it as a wrapped object, and then
 * unwrapping it back to original converter type.
 *
 * No additional magic but reducing an additional generic parameter nobody likes,
 * so instead of writing:
 * ```kotlin
 * ApiResult<ApiResponse<REAL_TYPE>, ERROR_TYPE>
 * ```
 * we can write:
 * ```kotlin
 * ApiResult<REAL_TYPE, ERROR_TYPE>
 * ```
 */
class ApiResponseConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        val rawType = getRawType(type)
        if (rawType == ApiError::class.java ||
            rawType == ApiResponse::class.java ||
            rawType == ApiResult::class.java
        ) return null

        val resultType = Types.newParameterizedType(ApiResponse::class.java, type)
        val delegateConverter = retrofit.nextResponseBodyConverter<ApiResponse<Any>>(
            this,
            resultType,
            annotations
        )
        return ApiResponseConverter(delegateConverter)
    }

    private class ApiResponseConverter(
        private val delegate: Converter<ResponseBody, ApiResponse<Any>>,
    ) : Converter<ResponseBody, Any?> {
        override fun convert(value: ResponseBody): Any? {
            return delegate.convert(value)?.message
        }
    }
}
