package edu.ucne.InsurePal.domain.usuario.useCases

import edu.ucne.InsurePal.data.Resource
import edu.ucne.InsurePal.domain.usuario.Usuario
import edu.ucne.InsurePal.domain.usuario.UsuarioRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObtenerUsuariosUseCase @Inject constructor(
    private val repository: UsuarioRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Usuario>>> = repository.getUsuarios()
}