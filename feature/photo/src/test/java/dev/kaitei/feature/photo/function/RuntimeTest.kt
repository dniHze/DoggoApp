package dev.kaitei.feature.photo.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.feature.photo.*
import kotlinx.coroutines.test.runBlockingTest
import oolong.Dispatch
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

internal class RuntimeTest {

    @Nested
    @DisplayName("init")
    inner class Init {

        @Test
        fun `on init creates loading state`() {
            val init = initRuntime("foo", "bar", MockRepo())
            val (state) = init()
            assertTrue(state.isLoading)
        }

        @Test
        fun `on init creates loading state with same ids`() {
            val init = initRuntime("foo", "bar", MockRepo())
            val (state) = init()
            assertEquals("foo", state.id)
            assertEquals("bar", state.fullName)
        }

        @Test
        fun `on init creates load data effect`() {
            val init = initRuntime(
                "foo",
                "bar",
                object : MockRepo() {
                    override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                        return ApiResult.apiFailure()
                    }
                }
            )
            val (_, effect) = init()
            runBlockingTest {
                effect.invoke(this) {
                    assertTrue(it is Msg.ApiFailure)
                }
            }
        }

        @Test
        fun `on init creates load data effect with same id`() {
            val repo = object : MockRepo() {
                var id: String? = null
                override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                    this.id = id
                    return ApiResult.apiFailure()
                }
            }
            val init = initRuntime("foo", "bar", repo)
            val (_, effect) = init()
            runBlockingTest {
                effect.invoke(this) {
                    assertTrue(it is Msg.ApiFailure)
                }
                assertEquals("foo", repo.id)
            }
        }
    }

    @Nested
    @DisplayName("update")
    inner class Update {

        @Test
        fun `load message creates loading state`() {
            val model = Model(
                id = "foo",
                fullName = "bar",
                isLoading = false
            )
            val update = update(MockRepo())
            val (state) = update.invoke(Msg.LoadData, model)
            assertTrue(state.isLoading)
        }

        @Test
        fun `load message creates loading state with load data effect`() {
            val update = update(object : MockRepo() {
                override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                    return ApiResult.apiFailure()
                }
            })
            val (_, effect) = update(
                Msg.LoadData,
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false
                )
            )
            runBlockingTest {
                effect.invoke(this) {
                    assertTrue(it is Msg.ApiFailure)
                }
            }
        }

        @Test
        fun `load message creates loading state with load data effect with same id`() {
            val repo = object : MockRepo() {
                var id: String? = null
                override suspend fun getBreedPhotos(id: String): ApiResult<List<String>, ApiError> {
                    this.id = id
                    return ApiResult.apiFailure()
                }
            }
            val update = update(repo)
            val (_, effect) = update(
                Msg.LoadData,
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false
                )
            )
            runBlockingTest {
                effect.invoke(this) {
                    assertTrue(it is Msg.ApiFailure)
                }
            }
        }

        @Test
        fun `api success message creates state with data`() {
            val update = update(MockRepo())
            val (model) = update(
                Msg.ApiSuccess(emptyList()), Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false
                )
            )
            assertEquals(emptyList<String>(), model.data)
        }

        @Test
        fun `api failure message creates state with non null failure`() {
            val update = update(MockRepo())
            val (model) = update(
                Msg.ApiFailure(ApiResult.apiFailure()),
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false
                )
            )
            assertNotNull(model.failure)
        }

        @Test
        fun `api failure message creates state with mapped failure`() {
            val update = update(MockRepo())
            val (model) = update(
                Msg.ApiFailure(ApiResult.apiFailure()),
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false
                )
            )
            assertTrue(model.failure is FailureReason.Api)
        }
    }

    @Nested
    @DisplayName("view")
    inner class View {
        @Test
        fun `model create props with same ids`() {
            val view = view(MockDirections())
            val model = Model(
                id = "foo",
                fullName = "bar",
                isLoading = true
            )
            val props = view(model)
            assertEquals(model.fullName, props.fullName)
        }

        @Test
        fun `model create props with go back action`() {
            val directions = object : MockDirections() {
                var called = false
                override fun goBack() {
                    called = true
                }
            }
            val view = view(directions)
            val model = Model(
                id = "foo",
                fullName = "bar",
                data = emptyList(),
                isLoading = false
            )
            val props = view(model)
            props.onBackPressed()
            assertTrue(directions.called)
        }

        @Test
        fun `loading model create loading props`() {
            val view = view(MockDirections())
            val props = view(
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = true
                )
            )
            assertEquals(DataProps.Loading, props.data)
        }

        @Test
        fun `error model create error props`() {
            val view = view(MockDirections())
            val props = view(
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false,
                    failure = FailureReason.Api(100, null)
                )
            )
            assertTrue(props.data is DataProps.Failure)
        }

        @Test
        fun `error model create error props with same reason`() {
            val view = view(MockDirections())
            val reason = FailureReason.Api(100, null)
            val props = view(
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false,
                    failure = reason
                )
            ).data as DataProps.Failure
            assertEquals(reason, props.reason)
        }

        @Test
        fun `error model create error props with callback dispatching load message`() {
            val view = view(MockDirections())
            val reason = FailureReason.Api(100, null)
            val props = view(
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false,
                    failure = reason
                )
            ).data as DataProps.Failure
            var calledCount = 0
            val dispatcher: Dispatch<Msg> = {
                calledCount++
                assertEquals(Msg.LoadData, it)
            }
            props.onRetryClicked(dispatcher)
            assertEquals(1, calledCount)
        }

        @Test
        fun `success model create success props`() {
            val view = view(MockDirections())
            val props = view(
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false,
                    data = emptyList()
                )
            )
            assertTrue(props.data is DataProps.Success)
        }

        @Test
        fun `success model create success props with same data`() {
            val data = emptyList<String>()
            val view = view(MockDirections())
            val props = view(
                Model(
                    id = "foo",
                    fullName = "bar",
                    isLoading = false,
                    data = data
                )
            ).data as DataProps.Success
            assertEquals(data, props.photos)
        }
    }
}