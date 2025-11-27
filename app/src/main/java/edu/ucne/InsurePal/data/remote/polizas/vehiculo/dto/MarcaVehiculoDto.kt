package edu.ucne.InsurePal.data.remote.polizas.vehiculo.dto

data class MarcaVehiculoDto(
    val nombre: String,
    val modelos: List<ModeloVehiculoDto>
)