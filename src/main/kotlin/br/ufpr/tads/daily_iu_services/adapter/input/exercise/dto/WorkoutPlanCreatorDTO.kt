package br.ufpr.tads.daily_iu_services.adapter.input.exercise.dto

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import org.jetbrains.annotations.NotNull

data class WorkoutPlanCreatorDTO(

    @field:NotNull
    val name: String,
    val description: String,

    @field:NotNull
    val workoutIds: Map<Int, Long>,

    @field:NotNull
    @field:Min(1)
    @field:Max(7)
    val daysPerWeek: Int,

    @field:NotNull
    val totalWeeks: Int,

    @field:NotNull
    @field:Min(1)
    val iciqScoreMin: Int,

    @field:NotNull
    @field:Max(12, message = "Casos com ICIQ acima de 12 devem ser avaliados por um profissional de saúde.")
    val iciqScoreMax: Int,

    @field:NotNull
    @field:Min(12, message = "Idade mínima para indicação de um plano de treino é 12 anos.")
    val ageMin: Int,

    @field:NotNull
    @field:Max(120, message = "Idade máxima para indicação de um plano de treino é 120 anos.")
    val ageMax: Int,
)
