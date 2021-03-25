package dev.kaitei.feature.list.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.kaitei.common.ui.ErrorView
import dev.kaitei.common.ui.LoadingView
import dev.kaitei.feature.list.ListViewModel
import dev.kaitei.feature.list.FailureReason
import dev.kaitei.feature.list.Msg
import dev.kaitei.feature.list.Props
import dev.kaitei.feature.list.R
import oolong.Dispatch

@Composable
fun BreedList(viewModel: ListViewModel = viewModel()) {
    val runtime by viewModel.state.collectAsState()
    val (props, dispatch) = runtime

    Scaffold(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.title_dog_breeds))
                },
                elevation = 4.dp
            )
        },
    ) { paddingValues ->
        when (props) {
            is Props.Loading -> LoadingState(Modifier.padding(paddingValues))
            is Props.Failure -> ErrorState(props, dispatch, Modifier.padding(paddingValues))
            is Props.Success -> LoadedState(props, Modifier.padding(paddingValues))
        }
    }
}

@Composable
internal fun LoadedState(
    props: Props.Success,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        for (entry in props.breeds) {
            stickyHeader {
                Text(
                    text = entry.segmentCapitalLetter,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface.copy(alpha = 0.8F))
                        .padding(16.dp)
                )
            }

            for (breed in entry.breeds) {
                item(breed.id) {
                    BreedItem(
                        breed = breed.displayName,
                        onClick = {
                            props.onBreedClicked(breed)
                        }
                    )
                }

                if (breed.subs.isEmpty()) continue

                items(breed.subs, key = { it.id }) { subBreed ->
                    SubBreedItem(
                        breed = breed.displayName,
                        subBreed = subBreed.displayName,
                        onClick = {
                            props.onBreedClicked(subBreed)
                        }
                    )
                }
            }
        }
    }
}

@Composable
internal fun LoadingState(modifier: Modifier = Modifier) {
    LoadingView(
        message = stringResource(id = R.string.loading_breeds),
        animationRes = R.raw.happy_doggo_1,
        modifier = modifier
    )
}

@Composable
internal fun ErrorState(
    props: Props.Failure,
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