package edu.ucne.InsurePal.presentation.listaReclamos

sealed interface ListaReclamosEvent {
    data object OnCargarReclamos : ListaReclamosEvent
    data class OnReclamoClick(val id: String) : ListaReclamosEvent
    data object OnErrorDismiss : ListaReclamosEvent
}