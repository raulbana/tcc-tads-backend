package br.ufpr.tads.daily_iu_services.adapter.input.user.dto

import br.ufpr.tads.daily_iu_services.domain.validator.PasswordPattern
import jakarta.validation.constraints.Email
import org.jetbrains.annotations.NotNull

data class ChangePasswordDTO(

    @field:NotNull("O código OTP não pode ser nulo")
    val otp: String,

    @field:Email(message = "Formato de e-mail inválido")
    @field:NotNull
    val email: String,

    @field:NotNull
    @field:PasswordPattern
    val newPassword: String
)