package edu.ucne.InsurePal.domain.polizas.vida

import edu.ucne.InsurePal.data.Resource
import kotlinx.coroutines.flow.Flow

interface SeguroVidaRepository {

    fun getSegurosVida(usuarioId : Int): Flow<Resource<List<SeguroVida>>>

    suspend fun getSeguroVidaById(id: String): Resource<SeguroVida>

    suspend fun saveSeguroVida(seguro: SeguroVida): Resource<SeguroVida>

    suspend fun updateSeguroVida(id: String, seguro: SeguroVida): Resource<SeguroVida>

    suspend fun delete(id: String) : Resource<Unit>
}