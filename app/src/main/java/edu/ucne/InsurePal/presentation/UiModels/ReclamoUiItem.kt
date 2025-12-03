package edu.ucne.InsurePal.presentation.UiModels

data class ReclamoUiItem(
    val id: String,
    val polizaId: String,
    val titulo: String,
    val fecha: String,
    val status: String,
    val descripcion: String,
    val tipo: TipoReclamo
)