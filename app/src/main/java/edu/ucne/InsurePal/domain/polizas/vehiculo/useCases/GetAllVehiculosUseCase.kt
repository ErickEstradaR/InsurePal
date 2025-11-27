package edu.ucne.InsurePal.domain.polizas.vehiculo.useCases

import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.polizas.vehiculo.model.SeguroVehiculo
import edu.ucne.InsurePal.domain.polizas.vehiculo.repository.SeguroVehiculoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetAllVehiculosUseCase @Inject constructor(
    private val repository: SeguroVehiculoRepository
) {
    operator fun invoke(): Flow<Resource<List<SeguroVehiculo>>> {
        return repository.getAllVehiculos()
    }
}