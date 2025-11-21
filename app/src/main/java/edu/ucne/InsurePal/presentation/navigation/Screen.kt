package edu.ucne.InsurePal.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Home : Screen("home")
}