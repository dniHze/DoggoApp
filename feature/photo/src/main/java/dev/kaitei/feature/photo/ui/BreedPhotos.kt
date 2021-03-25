package dev.kaitei.feature.photo.ui

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.kaitei.common.ui.ErrorView
import dev.kaitei.common.ui.LoadingView
import dev.kaitei.feature.photo.R
import dev.kaitei.feature.photo.PhotoViewModel
import dev.kaitei.feature.photo.FailureReason
import dev.kaitei.feature.photo.DataProps
import dev.kaitei.feature.photo.Msg
import oolong.Dispatch

@Composable
internal fun BreedPhotos(
    id: String,
    fullName: String,
    viewModel: PhotoViewModel = viewModel()
) {
    LaunchedEffect(id, fullName) {
        viewModel.setParams(id, fullName)
    }

    val runtime by viewModel.state.collectAsState()
    val (props, dispatch) = runtime

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = props.onBackPressed
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back_24dp),
                            contentDescription = stringResource(R.string.action_go_back)
                        )
                    }
                },
                title = {
                    Text(text = props.fullName)
                },
                elevation = 4.dp
            )
        },
    ) { paddingValues ->
        when (props.data) {
            is DataProps.Loading -> LoadingState(Modifier.padding(paddingValues))
            is DataProps.Failure -> ErrorState(
                props.data,
                dispatch,
                Modifier.padding(paddingValues)
            )
            is DataProps.Success -> LoadedState(props.data, Modifier.padding(paddingValues))
        }
    }
}

@Composable
internal fun LoadedState(
    props: DataProps.Success,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(140.dp),
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(props.photos) { photo ->
            PhotoItem(photo = photo)
        }
    }
}

@Composable
internal fun LoadingState(
    modifier: Modifier = Modifier
) {
    LoadingView(
        message = stringResource(id = R.string.loading_photos),
        animationRes = R.raw.astro_doggo,
        modifier
    )
}

@Composable
internal fun ErrorState(
    props: DataProps.Failure,
    dispatch: Dispatch<Msg>,
    modifier: Modifier = Modifier
) {
    val title = when (props.reason) {
        is FailureReason.Network -> stringResource(R.string.error_network_title)
        is FailureReason.Unknown -> stringResource(R.string.error_unknown_title)
        is FailureReason.Api -> stringResource(R.string.error_http_title)
    }

    val message = when (props.reason) {
        is FailureReason.Network -> stringResource(R.string.error_network_description)
        is FailureReason.Unknown -> stringResource(R.string.error_unknown_description)
        is FailureReason.Api -> {
            if (props.reason.message != null) {
                stringResource(
                    R.string.error_http_description_with_message,
                    props.reason.message
                )
            } else {
                stringResource(R.string.error_http_description)
            }
        }
    }

    ErrorView(
        title = title,
        message = message,
        animationRes = R.raw.angry_doggo,
        button = stringResource(R.string.action_retry),
        modifier = modifier
    ) {
        props.onRetryClicked(dispatch)
    }
}