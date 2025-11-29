package edu.ucne.InsurePal.presentation.admin.adminReclamosVidas


sealed interface ListaReclamosVidasEvent {
    data object OnCargar : ListaReclamosVidasEvent
    data class OnReclamoClick(val id: Int) : ListaReclamosVidasEvent
    data object OnErrorDismiss : ListaReclamosVidasEvent
}

