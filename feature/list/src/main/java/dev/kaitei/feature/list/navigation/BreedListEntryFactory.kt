package dev.kaitei.feature.list.navigation

import androidx.hilt.navigation.compose.hiltNavGraphViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.kaitei.doggo.navigation.NavEntryFactory
import dev.kaitei.feature.list.ListViewModel
import dev.kaitei.feature.list.ui.BreedList
import javax.inject.Inject

class BreedListEntryFactory @Inject constructor() : NavEntryFactory {

    override fun buildEntry(navGraphBuilder: NavGraphBuilder) {
        navGraphBuilder.composable(
            route = route,
            arguments = emptyList()
        ) {
            val viewModel = hiltNavGraphViewModel<ListViewModel>()
            BreedList(viewModel)
        }
    }

    companion object {
        const val route = "list"
    }
}