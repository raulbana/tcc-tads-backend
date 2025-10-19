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
    val iciqScoreRecommendation: Int
)
