package dev.kaitei.doggoapp.navigation

import androidx.navigation.NavController

interface NavControllerOwner {
    val navController: NavController?
}