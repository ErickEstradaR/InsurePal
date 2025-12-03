package edu.ucne.InsurePal.presentation.adminListaVidas

import edu.ucne.InsurePal.domain.polizas.vida.model.SeguroVida

data class LifeListUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val policies: List<SeguroVida> = emptyList(),
    val filteredPolicies: List<SeguroVida> = emptyList(),
    val searchQuery: String = "",
    val selectedPolicy: SeguroVida? = null,
    val isDetailVisible: Boolean = false
)