package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.jetbrains.annotations.NotNull

data class ExerciseAttributeDTO(
    val id: Long? = null,

    @field:NotNull
    val name: String,

    @field:NotEmpty
    @field:Size(max = 255, message = "A descrição não deve exceder 255 caracteres")
    val description: String,

    @field:NotNull
    @field:Schema(description = "Tipo do atributo: 1 - Benefício, 2 - Contraindicação")
    val type: Int
)
