package dev.kaitei.feature.list

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.kaitei.doggo.api.DoggoRepository
import dev.kaitei.feature.list.function.init
import dev.kaitei.feature.list.function.update
import dev.kaitei.feature.list.function.view
import dev.kaitei.feature.list.navigation.ListDirections
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import oolong.Dispatch
import oolong.runtime
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repo: DoggoRepository,
    private val directions: ListDirections
) : ViewModel() {

    private var runtimeJob: Job? = null

    private val _state = MutableStateFlow<Pair<Props, Dispatch<Msg>>>(
        Props.Loading to { }
    )

    override fun onCleared() {
        Timber.i("Destroying Oolong runtime")
        runtimeJob?.cancel()
    }

    @Composable
    internal fun oolongRuntime(): State<Pair<Props, Dispatch<Msg>>> {
        initRuntime()
        return _state.collectAsState()
    }

    private fun initRuntime() {
        if (runtimeJob == null) {
            synchronized(this) {
                if (runtimeJob == null) {
                    Timber.i("Initialize Oolong runtime")
                    runtimeJob = runtime(init(repo), update(repo), view(directions), ::render)
                }
            }
        }
    }

    private fun render(props: Props, dispatch: Dispatch<Msg>) {
        Timber.i("New display props: %s", props)
        _state.value = props to dispatch
    }
}