package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.domain.validator.PasswordPattern
import jakarta.validation.constraints.Email

data class LoginRequestDTO(
    @field:Email(message = "Formato de e-mail inválido")
    val email: String,

    @field:PasswordPattern
    val password: String
)