package dev.kaitei.doggo.api.retorift

import com.slack.eithernet.ApiResult
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import com.squareup.moshi.*
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.kaitei.doggo.api.Breeds
import dev.kaitei.doggo.api.service.DoggoApi
import dev.kaitei.doggo.api.adapter.BreedAdapter
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.api.retrofit.ApiResponseConverterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

class ApiResponseConverterFactoryTest {

    private val mockWebServer = MockWebServer()
    private val doggoService = buildService()

    @BeforeEach
    fun setup() {
        mockWebServer.start(port = 8080)
    }

    @AfterEach
    fun teardown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `successful response parses as unwrapped`() {
        mockWebServer.enqueue(
            defaultResponse()
                .setResponseCode(200)
                .setBody("""{ "status": "success", "message": ["foo"] }""")
        )

        runBlocking {
            val doggos = doggoService.getImagesForBreed("foo")
            assertTrue(doggos is ApiResult.Success)
        }
    }

    @Test
    fun `successful response parses unwrapped as breeds`() {
        mockWebServer.enqueue(
            defaultResponse()
                .setResponseCode(200)
                .setBody("""{ "status": "success", "message": { "foo": [] } }""")
        )
        runBlocking {
            val doggos = doggoService.getAllBreeds()
            val data = (doggos as ApiResult.Success).response
            assertTrue(data is Breeds)
        }
    }

    @Test
    fun assertFails() {
        mockWebServer.enqueue(
            defaultResponse()
                .setResponseCode(404)
                .setBody("""{ "status": "error", "message": "foo bar", "code": 404 }""")
        )
        runBlocking {
            val doggos = doggoService.getAllBreeds()
            val failure = doggos as ApiResult.Failure.HttpFailure
            assertNotNull(failure.error)
        }
    }

    @Test
    fun `fails with serialized type`() {
        mockWebServer.enqueue(
            defaultResponse()
                .setResponseCode(404)
                .setBody("""{ "status": "error", "message": "foo bar", "code": 404 }""")
        )
        runBlocking {
            val doggos = doggoService.getAllBreeds()
            val failure = doggos as ApiResult.Failure.HttpFailure
            assertTrue(failure.error is ApiError)
        }
    }

    @Test
    fun `fails with serialized type with same params`() {
        mockWebServer.enqueue(
            defaultResponse()
                .setResponseCode(404)
                .setBody("""{ "status": "error", "message": "foo bar", "code": 404 }""")
        )
        runBlocking {
            val doggos = doggoService.getAllBreeds()
            val failure = doggos as ApiResult.Failure.HttpFailure
            val error = failure.error as ApiError
            assertEquals(ApiError(code = 404, message = "foo bar", status = "error"), error)
        }
    }

    private fun buildService(): DoggoApi {
        val moshi = Moshi.Builder()
            .add(BreedAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .addCallAdapterFactory(ApiResultCallAdapterFactory)
            .addConverterFactory(ApiResultConverterFactory)
            .addConverterFactory(ApiResponseConverterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(
                OkHttpClient.Builder()
                    .readTimeout(300L, TimeUnit.MILLISECONDS)
                    .build()
            )
            .baseUrl("http://127.0.0.1:8080")
            .build()

        return retrofit.create()
    }

    private fun defaultResponse(): MockResponse = MockResponse()
        .addHeader("Content-Type", "application/json; charset=utf-8")
        .addHeader("Cache-Control", "no-cache")
}