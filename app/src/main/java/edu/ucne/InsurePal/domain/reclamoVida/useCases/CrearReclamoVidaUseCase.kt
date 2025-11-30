package edu.ucne.InsurePal.domain.reclamoVida.useCases

import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.reclamoVida.model.CrearReclamoVidaParams
import edu.ucne.InsurePal.domain.reclamoVida.model.ReclamoVida
import edu.ucne.InsurePal.domain.reclamoVida.repository.ReclamoVidaRepository
import jakarta.inject.Inject
import java.io.File

class CrearReclamoVidaUseCase @Inject constructor(
    private val repository: ReclamoVidaRepository
) {
    suspend operator fun invoke(params: CrearReclamoVidaParams): Resource<ReclamoVida> {
        return repository.crearReclamoVida(params)
    }
}