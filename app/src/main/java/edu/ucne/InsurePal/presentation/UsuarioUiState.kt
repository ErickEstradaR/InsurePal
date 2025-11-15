package edu.ucne.InsurePal.presentation

import edu.ucne.InsurePal.domain.Usuario

data class UsuarioUiState (
    val isLoading: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val userMessage: String? = null,
    val usuarioId : Int? = null,
    val userName: String = "",
    val password:String? = "",
)
