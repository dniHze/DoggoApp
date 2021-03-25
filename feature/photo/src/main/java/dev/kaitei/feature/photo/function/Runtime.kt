package dev.kaitei.feature.photo.function

import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.feature.photo.DataProps
import dev.kaitei.feature.photo.Model
import dev.kaitei.feature.photo.Msg
import dev.kaitei.feature.photo.Props
import dev.kaitei.feature.photo.navigation.PhotoDirections
import oolong.effect.none
import oolong.Effect

internal fun initRuntime(
    id: String,
    fullName: String,
    repo: DoggoRepository
): () -> Pair<Model, Effect<Msg>> = {
    Model(id = id, fullName = fullName, isLoading = true) to loadPhotos(id, repo)
}

internal fun update(repo: DoggoRepository): (Msg, Model) -> Pair<Model, Effect<Msg>> =
    { msg, model ->
        when (msg) {
            is Msg.LoadData -> model.copy(
                isLoading = true,
                failure = null,
                data = null
            ) to loadPhotos(model.id, repo)

            is Msg.ApiFailure -> model.copy(
                isLoading = false,
                failure = msg.failure.toFailureReason()
            ) to none()

            is Msg.ApiSuccess -> model.copy(
                isLoading = false,
                failure = null,
                data = msg.data
            ) to none()
        }
    }

internal fun view(directions: PhotoDirections): (Model) -> Props = { model ->
    val dataProps = when {
        model.isLoading -> DataProps.Loading
        model.failure != null -> DataProps.Failure(
            model.failure,
            onRetryClicked = { dispatch -> dispatch(Msg.LoadData) }
        )
        else -> DataProps.Success(model.data!!)
    }
    Props(model.fullName, dataProps, directions::goBack)
}