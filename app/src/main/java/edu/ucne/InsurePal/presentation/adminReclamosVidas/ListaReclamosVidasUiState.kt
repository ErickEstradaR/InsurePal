package edu.ucne.InsurePal.presentation.adminReclamosVidas

import edu.ucne.InsurePal.domain.reclamoVida.model.ReclamoVida

data class ListaReclamosVidasUiState (
    val isLoading: Boolean = false,
    val reclamos: List<ReclamoVida> = emptyList(),
    val error: String? = null
)