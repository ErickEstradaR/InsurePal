package edu.ucne.InsurePal.presentation.admin


data class AdminUiState (
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val totalPolicies: Int = 0,
    val totalVehicles: Int = 0,
    val totalLife: Int = 0,
    val activeCount: Int = 0,
    val pendingCount: Int = 0,
    val totalCoverageValue: Double = 0.0
)

