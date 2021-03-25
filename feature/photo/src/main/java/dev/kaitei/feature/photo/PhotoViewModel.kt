package dev.kaitei.feature.photo

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.feature.photo.function.init
import dev.kaitei.feature.photo.function.update
import dev.kaitei.feature.photo.function.view
import dev.kaitei.feature.photo.navigation.PhotoDirections
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import oolong.Dispatch
import oolong.runtime
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val repo: DoggoRepository,
    private val directions: PhotoDirections
) : ViewModel() {

    private var runtimeJob: Job? = null

    private val _state = MutableStateFlow<Pair<Props, Dispatch<Msg>>>(
        Props("", DataProps.Loading) { } to { }
    )

    override fun onCleared() {
        Timber.i("Destroying Oolong runtime")
        runtimeJob?.cancel()
    }

    @Composable
    internal fun oolongRuntime(
        id: String,
        fullName: String
    ): State<Pair<Props, Dispatch<Msg>>> {
        initRuntime(id, fullName)
        return _state.collectAsState()
    }

    private fun initRuntime(id: String, fullName: String) {
        if (runtimeJob == null) {
            synchronized(this) {
                if (runtimeJob == null) {
                    Timber.i("Initialize Oolong runtime")
                    runtimeJob =
                        runtime(init(id, fullName, repo), update(repo), view(directions), ::render)
                }
            }
        }
    }

    private fun render(props: Props, dispatch: Dispatch<Msg>) {
        Timber.i("New display props: %s", props)
        _state.value = props to dispatch
    }
}