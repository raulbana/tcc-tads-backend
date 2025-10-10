package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

data class LoginResponseDTO(
    val token: String,
    val user: UserDTO
)