package edu.ucne.InsurePal.domain.reclamoVida.useCases

import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.reclamoVida.model.ReclamoVida
import edu.ucne.InsurePal.domain.reclamoVida.repository.ReclamoVidaRepository
import jakarta.inject.Inject
import java.io.File

class CrearReclamoVidaUseCase @Inject constructor(
    private val repository: ReclamoVidaRepository
) {
    suspend operator fun invoke(
        polizaId: String,
        usuarioId: Int,
        nombreAsegurado: String,
        descripcion: String,
        lugarFallecimiento: String,
        causaMuerte: String,
        fechaFallecimiento: String,
        numCuenta: String,
        actaDefuncion: File
    ): Resource<ReclamoVida> {

        return repository.crearReclamoVida(
            polizaId = polizaId,
            usuarioId = usuarioId,
            nombreAsegurado = nombreAsegurado,
            descripcion = descripcion,
            lugarFallecimiento = lugarFallecimiento,
            causaMuerte = causaMuerte,
            fechaFallecimiento = fechaFallecimiento,
            numCuenta = numCuenta,
            actaDefuncion = actaDefuncion
        )
    }
}