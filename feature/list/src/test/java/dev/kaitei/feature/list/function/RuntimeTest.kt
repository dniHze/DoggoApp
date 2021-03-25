package dev.kaitei.feature.list.function

import com.slack.eithernet.ApiResult
import dev.kaitei.doggo.api.model.ApiError
import dev.kaitei.doggo.model.BaseBreed
import dev.kaitei.doggo.model.Breed
import dev.kaitei.feature.list.FailureReason
import dev.kaitei.feature.list.Model
import dev.kaitei.feature.list.Msg
import dev.kaitei.feature.list.Props
import dev.kaitei.feature.list.model.BreedListSegment
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
            val init = init(MockRepo())
            val (state) = init()
            assertTrue(state.isLoading)
        }

        @Test
        fun `on init creates load data effect`() {
            val init = init(object : MockRepo() {
                override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                    return ApiResult.apiFailure()
                }
            })
            val (_, effect) = init()
            runBlockingTest {
                effect.invoke(this) {
                    assertTrue(it is Msg.ApiFailure)
                }
            }
        }
    }

    @Nested
    @DisplayName("update")
    inner class Update {

        @Test
        fun `load message creates loading state`() {
            val model = Model(isLoading = false)
            val update = update(MockRepo())
            val (state) = update.invoke(Msg.LoadData, model)
            assertTrue(state.isLoading)
        }

        @Test
        fun `load message creates loading state with load data effect`() {
            val update = update(object : MockRepo() {
                override suspend fun getAllBreeds(): ApiResult<List<Breed>, ApiError> {
                    return ApiResult.apiFailure()
                }
            })
            val (_, effect) = update(Msg.LoadData, Model(isLoading = false))
            runBlockingTest {
                effect.invoke(this) {
                    assertTrue(it is Msg.ApiFailure)
                }
            }
        }

        @Test
        fun `api success message creates state with data`() {
            val update = update(MockRepo())
            val (model) = update(Msg.ApiSuccess(emptyList()), Model(isLoading = false))
            assertEquals(emptyList<BreedListSegment>(), model.data)
        }

        @Test
        fun `api failure message creates state with non null failure`() {
            val update = update(MockRepo())
            val (model) = update(Msg.ApiFailure(ApiResult.apiFailure()), Model(isLoading = false))
            assertNotNull(model.failure)
        }

        @Test
        fun `api failure message creates state with mapped failure`() {
            val update = update(MockRepo())
            val (model) = update(Msg.ApiFailure(ApiResult.apiFailure()), Model(isLoading = false))
            assertTrue(model.failure is FailureReason.Api)
        }
    }

    @Nested
    @DisplayName("view")
    inner class View {

        @Test
        fun `loading model create loading props`() {
            val view = view(MockDirections())
            val props = view(Model(isLoading = true))
            assertEquals(Props.Loading, props)
        }

        @Test
        fun `error model create error props`() {
            val view = view(MockDirections())
            val props = view(
                Model(
                    isLoading = false,
                    failure = FailureReason.Api(100, null)
                )
            )
            assertTrue(props is Props.Failure)
        }

        @Test
        fun `error model create error props with same reason`() {
            val view = view(MockDirections())
            val reason = FailureReason.Api(100, null)
            val props = view(
                Model(
                    isLoading = false,
                    failure = reason
                )
            ) as Props.Failure
            assertEquals(reason, props.reason)
        }

        @Test
        fun `error model create error props with callback dispatching load message`() {
            val view = view(MockDirections())
            val reason = FailureReason.Api(100, null)
            val props = view(
                Model(
                    isLoading = false,
                    failure = reason
                )
            ) as Props.Failure
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
                    isLoading = false,
                    data = emptyList()
                )
            )
            assertTrue(props is Props.Success)
        }

        @Test
        fun `success model create success props with same data`() {
            val data = emptyList<BreedListSegment>()
            val view = view(MockDirections())
            val props = view(
                Model(
                    isLoading = false,
                    data = data
                )
            ) as Props.Success
            assertEquals(data, props.breeds)
        }

        @Test
        fun `success model create success props with nav action`() {
            val directions = object : MockDirections() {
                var called = false
                override fun openPhotos(baseBreed: BaseBreed) {
                    called = true
                }
            }
            val view = view(directions)
            val props = view(
                Model(
                    isLoading = false,
                    data = emptyList()
                )
            ) as Props.Success
            props.onBreedClicked(Breed("foo", "foo", emptyList()))
            assertTrue(directions.called)
        }

        @Test
        fun `props calls nav with same basebreed`() {
            val directions = object : MockDirections() {
                var breed: BaseBreed? = null
                override fun openPhotos(baseBreed: BaseBreed) {
                    breed = baseBreed
                }
            }
            val view = view(directions)
            val props = view(
                Model(
                    isLoading = false,
                    data = emptyList()
                )
            ) as Props.Success
            val breed = Breed("foo", "foo", emptyList())
            props.onBreedClicked(breed)
            assertEquals(breed, directions.breed)
        }
    }
}