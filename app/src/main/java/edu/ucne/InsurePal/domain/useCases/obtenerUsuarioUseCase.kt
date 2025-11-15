package edu.ucne.InsurePal.domain.useCases

import edu.ucne.InsurePal.domain.UsuarioRepository
import javax.inject.Inject

class obtenerUsuarioUseCase @Inject constructor(private val repo : UsuarioRepository) {
    suspend operator fun invoke (id: Int?) = repo.getUsuario(id)
}