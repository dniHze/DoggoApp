package dev.kaitei.feature.list.function

import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.feature.list.Model
import dev.kaitei.feature.list.Msg
import dev.kaitei.feature.list.Props
import dev.kaitei.feature.list.navigation.ListDirections
import oolong.effect.none
import oolong.Effect

internal fun init(repo: DoggoRepository): () -> Pair<Model, Effect<Msg>> = {
    Model(isLoading = true, null, null) to loadBreads(repo)
}

internal fun update(repo: DoggoRepository): (Msg, Model) -> Pair<Model, Effect<Msg>> =
    { msg, model ->
        when (msg) {
            is Msg.LoadData -> Model(isLoading = true) to loadBreads(repo)

            is Msg.ApiFailure -> model.copy(
                isLoading = false,
                failure = msg.failure.toFailureReason()
            ) to none()

            is Msg.ApiSuccess -> model.copy(
                isLoading = false,
                failure = null,
                data = msg.data.asSortedBreedSegments()
            ) to none()
        }
    }

internal fun view(directions: ListDirections): (Model) -> Props = { model ->
    when {
        model.isLoading -> Props.Loading
        model.failure != null -> Props.Failure(
            model.failure,
            onRetryClicked = { dispatch -> dispatch(Msg.LoadData) }
        )
        else -> Props.Success(model.data!!, directions::openPhotos)
    }
}