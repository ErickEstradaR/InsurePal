package edu.ucne.InsurePal.domain.reclamoVida.useCases

import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.reclamoVida.model.ReclamoVida
import edu.ucne.InsurePal.domain.reclamoVida.repository.ReclamoVidaRepository
import jakarta.inject.Inject

class CambiarEstadoReclamoVidaUseCase @Inject constructor(
    private val repository: ReclamoVidaRepository
) {
    suspend operator fun invoke(
        reclamoId: String,
        nuevoEstado: String,
        motivoRechazo: String?
    ): Resource<ReclamoVida> {

        if (nuevoEstado == "RECHAZADO" && motivoRechazo.isNullOrBlank()) {
            return Resource.Error("Para rechazar un reclamo debes especificar el motivo.")
        }

        return repository.cambiarEstadoReclamoVida(reclamoId, nuevoEstado, motivoRechazo)
    }
}