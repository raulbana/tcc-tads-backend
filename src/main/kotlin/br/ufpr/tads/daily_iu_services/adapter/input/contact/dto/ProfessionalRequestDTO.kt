package br.ufpr.tads.daily_iu_services.adapter.input.contact.dto

import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

data class ProfessionalRequestDTO (
    @field:NotNull
    val name: String,

    @field:NotNull
    val email: String,

    @field:NotBlank
    val reason: String
)