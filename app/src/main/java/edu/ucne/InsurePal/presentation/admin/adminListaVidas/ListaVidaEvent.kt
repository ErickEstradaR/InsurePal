package edu.ucne.InsurePal.presentation.admin.adminListaVidas

import edu.ucne.InsurePal.domain.polizas.vida.model.SeguroVida

sealed interface ListaVidaEvent {
    data class OnSearchQueryChange(val query: String) : ListaVidaEvent
    data class OnSelectPolicy(val policy: SeguroVida) : ListaVidaEvent
    data object OnDismissDetail : ListaVidaEvent
    data object Refresh : ListaVidaEvent
}