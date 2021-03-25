package dev.kaitei.doggo.navigation

import androidx.navigation.NavGraphBuilder

interface NavEntryFactory {
    fun buildEntry(navGraphBuilder: NavGraphBuilder)
}