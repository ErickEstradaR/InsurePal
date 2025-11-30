package edu.ucne.InsurePal.domain.reclamoVehiculo.model

import java.io.File

data class CrearReclamoVehiculoParams(
    val polizaId: String,
    val usuarioId: Int,
    val descripcion: String,
    val direccion: String,
    val tipoIncidente: String,
    val fechaIncidente: String,
    val numCuenta: String,
    val imagen: File
)